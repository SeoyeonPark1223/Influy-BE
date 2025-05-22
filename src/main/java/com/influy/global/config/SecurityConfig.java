package com.influy.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors-> cors.configurationSource(CorsConfig.corsConfigurationSource()))
                .csrf(auth->auth.disable());
        http
                .authorizeHttpRequests(auth->auth
                        .anyRequest().permitAll()); //로그인 기능 구현 전까지 일단 모두 허용

        return http.build();
    }
}
