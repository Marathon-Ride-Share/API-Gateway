package com.marathonrideshare.apigateway.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() 
            .authorizeRequests()
                .antMatchers("/public/**").permitAll() 
                .anyRequest().authenticated() 
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); 

        http.addFilter(new JwtTokenAuthenticationFilter(authenticationManager()));
        http.addFilterBefore(new JwtTokenVerifier(), UsernamePasswordAuthenticationFilter.class);
    }
}
