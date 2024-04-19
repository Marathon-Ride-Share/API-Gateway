package com.marathonrideshare.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/in-ride-chat/send")  // Handle send specifically
                        .filters(f -> f.rewritePath("/api/in-ride-chat/send", "/chat/send"))
                        .uri("http://localhost:8081"))
                .route(p -> p
                        .path("/api/in-ride-chat/receive")  // Handle receive specifically
                        .filters(f -> f.rewritePath("/api/in-ride-chat/receive", "/chat/receive"))
                        .uri("http://localhost:8081"))
                .build();
    }

}
