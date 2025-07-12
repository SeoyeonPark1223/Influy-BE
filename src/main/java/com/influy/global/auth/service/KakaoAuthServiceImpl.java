package com.influy.global.auth.service;

import com.influy.domain.member.entity.Member;
import com.influy.domain.member.service.MemberService;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.auth.converter.AuthConverter;
import com.influy.global.auth.dto.AuthRequestDTO;
import com.influy.global.auth.dto.AuthResponseDTO;
import com.influy.global.jwt.CookieUtil;
import com.influy.global.jwt.JwtTokenProvider;
import com.influy.global.redis.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;

import static com.influy.global.util.StaticValues.REFRESH_EXPIRE;

@Service
@RequiredArgsConstructor
public class KakaoAuthServiceImpl implements AuthService {


    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;
    @Value("${social.redirect-uri}")
    private String redirectUri;

    @Override
    public AuthResponseDTO.KakaoLoginResponse SocialLogIn(String code, HttpServletResponse response) {


        //토큰 받기 POST 요청

        RestClient restClient = RestClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8").build();

        AuthRequestDTO.KakaoToken result = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", kakaoRestApiKey)
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("code", code)
                        .build())
                .retrieve()
                .body(AuthRequestDTO.KakaoToken.class);

        Long kakaoId = GetSocialUserId(Objects.requireNonNull(result).getAccess_token());

        //멤버 찾아 서비스 토큰 발급(카카오 토큰과 다름)
        try {
            Member member = memberService.findByKakaoId(kakaoId);

            //토큰 발급
            String[] tokenPair = issueToken(member);
            String accessToken = tokenPair[0];
            String refreshToken = tokenPair[1];

            //쿠키에 담기
            CookieUtil.refreshTokenInCookie(response,refreshToken);

            return AuthConverter.toTokenPair(member.getId(), accessToken);

        } catch (GeneralException e) {
            return AuthConverter.toRequestSignUp(kakaoId);
        }


    }

    @Override
    public Long GetSocialUserId(String accessToken) {


        RestClient restClient = RestClient.create();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        AuthRequestDTO.KakaoUser dto = restClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .body(AuthRequestDTO.KakaoUser.class);


        return Long.parseLong(dto.getId());
    }

    @Override
    public String[] issueToken(Member member) {

        String[] tokenPair = new String[2];

        Long memberId = member.getId();

        String accessToken = jwtTokenProvider.generateAccessToken(memberId, member.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(memberId);
        // Redis에 refresh token 저장
        redisService.setValue(member.getUsername(), refreshToken, REFRESH_EXPIRE);

        tokenPair[0] = accessToken;
        tokenPair[1] = refreshToken;

        return tokenPair;
    }

    @Override
    public String[] reissueToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = CookieUtil.extractRefreshTokenFromCookie(request);
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            System.out.println(refreshToken);
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }

        // 2. refreshToken에서 id 추출
        Long memberId = jwtTokenProvider.getId(refreshToken);

        Member member = memberService.findById(memberId);

        // 3. DB에 저장된 refreshToken과 비교 (추가 구현 필요)
        if(!redisService.getValue(member.getUsername()).equals(refreshToken)){
            System.out.println(redisService.getValue(member.getUsername()));
            System.out.println(refreshToken);
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }

        return issueToken(member);
    }

}
