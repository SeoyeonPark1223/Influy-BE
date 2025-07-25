package com.influy.global.auth.service;

import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.domain.member.repository.MemberRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.auth.TokenPair;
import com.influy.global.auth.converter.AuthConverter;
import com.influy.global.auth.dto.AuthRequestDTO;
import com.influy.global.auth.dto.AuthResponseDTO;
import com.influy.global.jwt.CookieUtil;
import com.influy.global.jwt.JwtAuthenticationFilter;
import com.influy.global.jwt.JwtTokenProvider;
import com.influy.global.redis.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Objects;

import static com.influy.global.util.StaticValues.REFRESH_EXPIRE;

@Service
@RequiredArgsConstructor
public class KakaoAuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final MemberRepository memberRepository;

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;
    @Value("${social.redirect-uri}")
    private String redirectUri;
    private final String prodRedirectUri = "https://influy.com/oauth/kakao/callback";
    @Value("${kakao.admin-key}")
    private String kakaoAdminKey;

    @Override
    public AuthResponseDTO.LoginResponse SocialLogIn(String code, HttpServletResponse response, Boolean redirectToLocal) {


        //토큰 받기 POST 요청

        RestClient restClient = RestClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8").build();

        AuthRequestDTO.KakaoToken result = restClient.post()
                .uri(uriBuilder -> uriBuilder.path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", kakaoRestApiKey)
                        .queryParam("redirect_uri", redirectToLocal ? redirectUri : prodRedirectUri)
                        .queryParam("code", code)
                        .build())
                .retrieve()
                .body(AuthRequestDTO.KakaoToken.class);

        Long kakaoId = GetSocialUserId(Objects.requireNonNull(result).getAccess_token());

        //멤버 찾아 서비스 토큰 발급(카카오 토큰과 다름)
        try {
            Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(()->new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

            //토큰 발급
            TokenPair tokenPair = issueToken(member);

            //쿠키에 담기
            CookieUtil.refreshTokenInCookie(response,tokenPair.refreshToken());

            if(member.getRole()== MemberRole.USER){
                return AuthConverter.toUserIdAndTokenDto(member.getId(), tokenPair.accessToken());
            }else{
                return AuthConverter.toSellerIdAndToken(member.getId(), Objects.requireNonNull(member.getSellerProfile()).getId(),tokenPair.accessToken());
            }


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
    public TokenPair issueToken(Member member) {

        Long memberId = member.getId();

        String accessToken = jwtTokenProvider.generateAccessToken(memberId, member.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(memberId);
        // Redis에 refresh token 저장
        redisService.setValue(member.getUsername(), refreshToken, REFRESH_EXPIRE);



        return new TokenPair(accessToken,refreshToken);
    }

    @Override
    public TokenPair reissueToken(HttpServletRequest request, HttpServletResponse response) {

        // 1. 쿠키에서 토큰 가져오기
        String refreshToken = CookieUtil.extractRefreshTokenFromCookie(request);
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }

        // 2. refreshToken에서 id 추출
        Long memberId = jwtTokenProvider.getId(refreshToken);

        Member member = memberRepository.findById(memberId).orElseThrow(()->new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 3. Redis에 저장된 refreshToken과 비교
        if(!redisService.getValue(member.getUsername()).equals(refreshToken)){
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }

        return issueToken(member);
    }

    @Override
    public void signOut(HttpServletRequest request, Member member) {
        if(member==null) throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);

        String accessToken = jwtAuthenticationFilter.resolveToken(request);

        // 로그아웃 시 refreshToken을 redis에서 삭제하고 accessToken을 redis에 저장
        redisService.deleteValue(member.getUsername()); // 재로그인 방지
        redisService.setValue(accessToken, "logout", jwtTokenProvider.getExpirationTime(accessToken)); // AccessToken을 블랙리스트에 저장
    }

    @Override
    public AuthRequestDTO.KakaoUserProfile getUserProfile(Long kakaoId) {
        RestClient restClient = RestClient.create();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoAdminKey);
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");


        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", kakaoId.toString());
        body.add("property_keys", "[\"kakao_account.profile\"]");

        return restClient.post()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(body)
                .retrieve()
                .body(AuthRequestDTO.KakaoUserProfile.class);
    }

}
