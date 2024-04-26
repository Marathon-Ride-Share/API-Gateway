//package com.marathonrideshare.apigateway.filters;
//
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpStatus;
//import reactor.core.publisher.Mono;
//import java.security.Key;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import java.nio.charset.StandardCharsets;
//import org.springframework.http.HttpHeaders;

package com.marathonrideshare.apigateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import java.security.Key;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpHeaders;
import java.util.Optional;
import org.springframework.http.HttpCookie;
// rest of your code


@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final String SECRET_KEY = System.getenv("JWT_SECRET") != null ? System.getenv("JWT_SECRET") : "marathon-ride-share-applications-secret-key";
    private static final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

       String path = exchange.getRequest().getPath().toString();
       String[] pathValues = path.split("/");

       if (pathValues[1].equals("users") || pathValues[1].equals("chats") || pathValues[1].equals("rides") || pathValues[1].equals("reviews") || pathValues[1].equals("payments")) {
           return chain.filter(exchange);
       } else {

            System.out.println("Checking for token cookie..." + exchange.getRequest().getCookies());

            HttpCookie cookie = exchange.getRequest().getCookies().getFirst("token");


            if (cookie != null && !cookie.getValue().isEmpty()) {
                String token = cookie.getValue();

                try {
                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(SIGNING_KEY)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
                    System.out.println("Authorized via token from cookie!");

                } catch (Exception e) {
                    System.out.println("Authentication failed: " + e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            } else {
                System.out.println("No token cookie found, unauthorized request.");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                HttpHeaders headers = exchange.getResponse().getHeaders();
                System.out.println("Response Headers: " + headers);
            }));
       }
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE; // Maintain high precedence for security filtering
    }

}
