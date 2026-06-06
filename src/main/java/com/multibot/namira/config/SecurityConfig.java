package com.multibot.namira.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**").permitAll() // Ελεύθερη πρόσβαση στο αρχικό
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.loginPage("/").permitAll()) // Δικό σας login
                .oauth2Login(oauth -> oauth.loginPage("/")); // Google μόνο αν πατήσουν το κουμπί

        return http.build();
    }
}