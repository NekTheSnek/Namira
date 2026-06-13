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
                        // Επιτρέπουμε την πρόσβαση στη home και στα assets
                        .requestMatchers("/", "/css/**", "/images/**").permitAll()
                        // Οποιοδήποτε άλλο URL (όπως το /secureaccess) απαιτεί login
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/secureaccess", true) // Μετά το login, στείλτον στο /secureaccess
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // Μετά το logout, στείλτον στη home
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}