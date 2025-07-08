package com.influy.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt;
        try {
            jwt = resolveToken(request);

            //빈문자열 ""과 whiteSpace 로만 이루어진 문자열도 불허
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // JWT에서 Authentication 객체 생성(아직 인증 전)
                Authentication authentication = this.tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request,response);
        }
        catch (JwtException e) {
            log.error("[JWTExceptionHandlerFilter] " + e.getMessage());
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - " + e.getMessage());
        } catch (Exception e) {
            log.error("[ExceptionHandlerFilter] " + e.getMessage());
            writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Error processing request - " + e.getMessage());
        }
    }

    //요청의 헤더로부터 토큰 추출하는 함수
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && StringUtils.startsWithIgnoreCase(bearerToken,
                "Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    //에러 확인 용
    private void writeErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", message);

        objectMapper.writeValue(response.getWriter(), errorBody);
    }


}


