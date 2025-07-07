package com.influy.global.auth.dto;

import lombok.*;

public class AuthResponseDTO {

    public sealed interface KakaoLoginResponse permits TokenPair,RequestSignUp{

    }
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static non-sealed class TokenPair implements KakaoLoginResponse {
        private Long memberId;
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static non-sealed class RequestSignUp implements KakaoLoginResponse {
        private String message;
        private Long kakaoId;
    }
}
