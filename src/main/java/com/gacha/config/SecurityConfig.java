// package com.gacha.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class SecurityConfig {
//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             // .csrf(csrf -> csrf.disable()) // Disable CSRF for testing
//             .cors(cors -> cors.disable()) // Spring Security might override CORS settings
//             .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
//         return http.build();
//     }
// }
