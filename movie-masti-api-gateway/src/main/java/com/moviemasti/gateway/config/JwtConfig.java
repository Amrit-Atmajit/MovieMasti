package com.moviemasti.gateway.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    // Default secret (not recommended for production)
    private String secret = "U2VjdXJlS2V5Rm9ySldUU2lnbmluZ0tleVdpdGhBdE1vc3Q2NENoYXJhY3RlcnM="; // TODO: Remove this default secret
    //private String secret;
    @PostConstruct
    public void init() {
        if (secret == null || secret.trim().isEmpty()) {
            System.err.println("WARNING: Using default JWT secret. This is not recommended for production!");
            secret = "U2VjdXJlS2V5Rm9ySldUU2lnbmluZ0tleVdpdGhBdE1vc3Q2NENoYXJhY3RlcnM="; // TODO: Remove this default secret
        }
        System.out.println("JWT Config initialized with secret length: " + secret.length());
    }
}
