package com.moviemasti.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/user/**")
                        .uri("lb://user-service"))
                .route("movie-service", r -> r.path("/api/movies/**")
                        .uri("lb://movie-service"))
                .route("showtime-service", r -> r.path("/api/showtimes/**")
                        .uri("lb://showtime-service"))
                .route("booking-service", r -> r.path("/api/bookings/**")
                        .uri("lb://booking-service"))
                .build();
    }
}
