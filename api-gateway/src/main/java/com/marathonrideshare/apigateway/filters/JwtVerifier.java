package com.marathonrideshare.apigateway.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.List;
import java.util.function.Function;

public class JwtVerifier extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey("secret_key") 
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                var authorities = (List<Map<String, String>>) claims.get("authorities");

            
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities.stream().map(a -> new SimpleGrantedAuthority(a.get("authority"))).collect(Collectors.toList())
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
