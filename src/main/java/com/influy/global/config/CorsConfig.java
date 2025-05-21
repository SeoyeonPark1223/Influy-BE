package com.influy.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public static CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // 허용할 Origin
        corsConfig.addAllowedOriginPattern("http://localhost:5173");
        corsConfig.addAllowedOriginPattern("https://influy.com");
        corsConfig.addAllowedOriginPattern("https://www.influy.com");

        // 요청 메서드, 헤더
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");

        // 인증정보 허용 (쿠키, Authorization 헤더 등)
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }
}