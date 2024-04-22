package com.marathonrideshare.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import com.marathonrideshare.apigateway.filters.JwtAuthenticationFilter;

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
                        .path("/api/users/login")
                        .filters(f -> f.rewritePath("/api/users/(?<segment>.*)", "/user/${segment}"))
                        .uri("http://localhost:8060"))
                .route(p -> p
                        .path("/api/users/register")
                        .filters(f -> f.rewritePath("/api/users/(?<segment>.*)", "/user/${segment}"))
                        .uri("http://localhost:8060"))
                .route(p -> p
                        .path("/api/users/**")
                        .filters(f -> f.rewritePath("/api/users/(?<segment>.*)", "/user/${segment}")
                        .uri("http://localhost:8060")))
                .route(p -> p
                        .path("/api/chat/**")
                        .filters(f -> f.rewritePath("/api/chat/(?<segment>.*)", "/chat/${segment}")
                        .uri("http://localhost:8081")))
                .build();
    }
}
