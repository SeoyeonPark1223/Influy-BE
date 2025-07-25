package com.influy.global.config;

import com.influy.global.jwt.JwtAuthenticationFilter;
import com.influy.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //cors 설정
                .cors(cors-> cors.configurationSource(CorsConfig.corsConfigurationSource()))
                .csrf(auth->auth.disable())
                //세션 설정
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //인가
                .authorizeHttpRequests(auth->auth
                        .requestMatchers(
                                "/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/home/**"
                        ).permitAll()
                        .requestMatchers("/oauth/**").permitAll()
                        .requestMatchers("/member/register/**","member/*/profile","member/auth/reissue").permitAll()
                        .requestMatchers(HttpMethod.GET,"/seller/items/*/questions/**").authenticated()
                        .requestMatchers(HttpMethod.GET,"/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
