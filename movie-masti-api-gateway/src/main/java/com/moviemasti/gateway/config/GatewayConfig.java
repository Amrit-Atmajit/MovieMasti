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
                // Route for /api/user/**
                .route("user-service-api", r -> r.path("/api/user/**")
                        .filters(f -> f.rewritePath("/api/(?<segment>.*)", "/${segment}"))
                        .uri("lb://user-service"))
                // Route for /user-service/api/**
                .route("user-service-prefixed", r -> r.path("/user-service/api/**")
                        .filters(f -> f.rewritePath("/user-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://user-service"))
                
                // Movie service routes
                .route("movie-service-api", r -> r.path("/api/movies/**")
                        .filters(f -> f.rewritePath("/api/(?<segment>.*)", "/${segment}"))
                        .uri("lb://movie-service"))
                .route("movie-service-prefixed", r -> r.path("/movie-service/api/**")
                        .filters(f -> f.rewritePath("/movie-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://movie-service"))
                
                // Showtime service routes
                .route("showtime-service-api", r -> r.path("/api/showtimes/**")
                        .filters(f -> f.rewritePath("/api/(?<segment>.*)", "/${segment}"))
                        .uri("lb://showtime-service"))
                .route("showtime-service-prefixed", r -> r.path("/showtime-service/api/**")
                        .filters(f -> f.rewritePath("/showtime-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://showtime-service"))
                
                // Booking service routes
                .route("booking-service-api", r -> r.path("/api/bookings/**")
                        .filters(f -> f.rewritePath("/api/(?<segment>.*)", "/${segment}"))
                        .uri("lb://booking-service"))
                .route("booking-service-prefixed", r -> r.path("/booking-service/api/**")
                        .filters(f -> f.rewritePath("/booking-service/(?<segment>.*)", "/${segment}"))
                        .uri("lb://booking-service"))
                .build();
    }
}
