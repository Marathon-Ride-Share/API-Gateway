package com.marathonrideshare.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import com.marathonrideshare.apigateway.filters.JwtAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, JwtAuthenticationFilter JwtFilter) {
        return builder.routes()
                .route(p -> p
                        .path("users/login")
                        .filters(f -> f.rewritePath("users/login/(?<segment>.*)", "users/login/${segment}"))
                        .uri("http://localhost:8060"))
                .route(p -> p
                        .path("users/register")
                        .filters(f -> f.rewritePath("users/register/(?<segment>.*)", "users/register/${segment}"))
                        .uri("http://localhost:8060"))
                .route(p -> p
                        .path("/users/**")
                        .filters(f -> f.rewritePath("/users/(?<segment>.*)", "/users/${segment}"))
                        .uri("http://localhost:8060"))
                .route(p -> p
                        .path("/chat/**")
                        .filters(f -> f.rewritePath("/chat/(?<segment>.*)", "/chat/${segment}"))
                        .uri("http://localhost:8083"))
                .route(p -> p
                        .path("/rides/**")
                        .filters(f -> f.rewritePath("/rides/(?<segment>.*)", "/rides/${segment}"))
                        .uri("http://localhost:8082"))
                .route(p -> p
                        .path("/reviews/**")
                        .filters(f -> f.rewritePath("/reviews/(?<segment>.*)", "/reviews/${segment}"))
                        .uri("http://localhost:8085"))
                .build();
    }

}
