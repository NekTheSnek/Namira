package com.multibot.namira.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**").permitAll() // Αφήνουμε τη Home ελεύθερη
                        .anyRequest().authenticated()               // Όλα τα άλλα θέλουν login
                )
                // Εδώ λέμε στο Spring: "Μην δείχνεις τη δική σου σελίδα login"
                // Χρησιμοποίησε το oauth2Login() μόνο για το redirect στη Google
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/secureaccess", true)
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}