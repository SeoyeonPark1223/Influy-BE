package com.influy.global.jwt;

import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.domain.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static com.influy.global.util.StaticValues.ACCESS_TOKEN_EXPIRE;
import static com.influy.global.util.StaticValues.REFRESH_EXPIRE;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final MemberService memberService;

    /**
     * 토큰 생성, 토큰 검증, ID 추출
     */

    @Value("${jwt.secret}")
    private String secret;

    private Key key;


    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    //accessToken 생성
    public String generateAccessToken(Long memberId, MemberRole role) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("role", role.getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //refreshToken 생성
    public String generateRefreshToken(Long memberId) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRE))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //id 추출
    public Long getId(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return Long.valueOf(claims.getSubject());
    }


    public Authentication getAuthentication(String token) {
        Long id = getId(token);
        Member member = memberService.findById(id);
        UserDetails userDetails = new CustomUserDetails(member);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        return auth;


    }

    public Long getExpirationTime(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().getTime();
    }
}
