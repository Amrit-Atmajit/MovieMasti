package com.moviemasti.gateway.utils;

import com.moviemasti.gateway.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter {

    private final JwtConfig jwtConfig;

    @Autowired
    public JwtAuthFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    private static final List<String> PUBLIC_ROUTES = List.of(
            "/api/user/register",
            "/api/user/login",
            "/api/user/test"
    );

    @PostConstruct
    public void init() {
        String secret = jwtConfig.getSecret();
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalStateException("JWT secret is not configured. Please check your application.yml");
        }
        System.out.println("JWT Secret loaded. Length: " + secret.length());
    }

    private Key getSigningKey() {
        try {
            if (jwtConfig.getSecret() == null) {
                throw new IllegalStateException("JWT secret is null. Check your application.yml configuration.");
            }

            String trimmedSecret = jwtConfig.getSecret().trim();
            if (trimmedSecret.isEmpty()) {
                throw new IllegalArgumentException("JWT secret is empty after trimming");
            }

            try {
                byte[] keyBytes = Decoders.BASE64.decode(trimmedSecret);
                System.out.println("JWT Secret key length: " + keyBytes.length + " bytes");

                if (keyBytes.length < 32) {
                    throw new IllegalArgumentException(String.format(
                            "JWT secret key must be at least 256 bits (32 bytes) long, but was %d bytes",
                            keyBytes.length));
                }

                Key key = Keys.hmacShaKeyFor(keyBytes);
                System.out.println("Successfully created JWT signing key");
                return key;
            } catch (IllegalArgumentException e) {
                if (e.getMessage().contains("Illegal base64 character")) {
                    throw new IllegalArgumentException("JWT secret is not a valid Base64 string. Please check your configuration.", e);
                }
                throw e;
            }
        } catch (Exception e) {
            System.err.println("Failed to create JWT signing key: " + e.getMessage());
            throw new RuntimeException("Failed to create JWT signing key. Please check your JWT secret configuration in application.yml. Error: " + e.getMessage(), e);
        }
    }

    private boolean isPublicRoute(String path) {
        return PUBLIC_ROUTES.stream().anyMatch((route) -> path.startsWith(route));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getURI().getPath();

        // Skip JWT validation for public routes
        if (isPublicRoute(requestPath)) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange.getRequest().getHeaders());

        if (token == null || !validateToken(token)) {
            return unauthorizedResponse(exchange);
        }

        Claims claims = extractClaims(token);
        exchange = addHeaders(exchange, claims);

        return chain.filter(exchange);
    }

    private String extractToken(HttpHeaders headers) {
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                System.err.println("JWT token is null or empty");
                return false;
            }

            // Try to parse the token with the signing key
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (io.jsonwebtoken.security.WeakKeyException e) {
            System.err.println("JWT Error: " + e.getMessage());
            /*System.err.println("Current JWT Secret (first 10 chars): " +
                    (secret != null && secret.length() > 10 ?
                            secret.substring(0, 10) + "..." : "null or too short"));*/
            return false;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.err.println("JWT Token expired: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("JWT Validation Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private ServerWebExchange addHeaders(ServerWebExchange exchange, Claims claims) {
        String userId = claims.getSubject(); //`sub` in JWT is the user ID
        String role = claims.get("role", String.class);

        return exchange.mutate()
                .request(builder -> builder
                        .header("X-User-Id", userId)
                        .header("X-User-Role", role))
                .build();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

//    API Gateway whenever gets requests, validates the token coming from header with the same jwt secret used
//    in user-service. If it matches proceeds to respected service or else UNAUTHORIZED
}
