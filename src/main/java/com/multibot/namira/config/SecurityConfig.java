package com.multibot.namira.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login").permitAll() // Όλοι βλέπουν την αρχική
                        .anyRequest().authenticated() // Για όλα τα άλλα θέλει Login
                )
                .oauth2Login(withDefaults()) // Ενεργοποίηση Google/Discord Login
                .logout(logout -> logout.logoutSuccessUrl("/"));

        return http.build();
    }
}