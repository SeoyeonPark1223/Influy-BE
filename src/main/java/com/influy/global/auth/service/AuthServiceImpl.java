package com.influy.global.auth.service;

import com.influy.domain.member.entity.Member;
import com.influy.domain.member.service.MemberService;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.auth.converter.AuthConverter;
import com.influy.global.auth.dto.AuthRequestDTO;
import com.influy.global.auth.dto.AuthResponseDTO;
import com.influy.global.jwt.JwtTokenProvider;
import com.influy.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.influy.global.util.StaticValues.REFRESH_EXPIRE;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {



    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;
    @Value("${social.redirect-uri}")
    private String redirectUri;
    @Override
    public AuthResponseDTO.KakaoLoginResponse kakaoSignIn(String code) {



        //토큰 받기 POST 요청

        RestClient restClient = RestClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8").build();

        AuthRequestDTO.KakaoToken result = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/oauth/token")
                        .queryParam("grant_type","authorization_code")
                        .queryParam("client_id",kakaoRestApiKey)
                        .queryParam("redirect_uri",redirectUri)
                        .queryParam("code", code)
                        .build())
                .retrieve()
                .body(AuthRequestDTO.KakaoToken.class);

        Long kakaoId = getKakaoUserID(Objects.requireNonNull(result).getAccess_token());

        //멤버 찾아 서비스 토큰 발급(카카오 토큰과 다름)
        try {
            Member member = memberService.findByKakaoId(kakaoId);

            //토큰 발급
            return issueToken(member);

        } catch(GeneralException e){
            return AuthConverter.toRequestSignUp(kakaoId);
        }


    }

    @Override
    public Long getKakaoUserID(String accessToken) {


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
    public AuthResponseDTO.TokenPair issueToken(Member member) {

        String accessToken = jwtTokenProvider.generateAccessToken(member.getKakaoId(), member.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        // Redis에 refresh token 저장
        redisService.setValue(member.getUsername(), refreshToken, 1000 * REFRESH_EXPIRE); // Timeout= 밀리초라서 1000 곱하기

        return AuthConverter.toTokenPair(member.getId(),accessToken,refreshToken);
    }
}
