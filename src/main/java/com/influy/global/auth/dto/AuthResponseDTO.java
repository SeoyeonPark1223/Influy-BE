package com.influy.global.auth.dto;

import lombok.*;

public class AuthResponseDTO {

    public sealed interface KakaoLoginResponse permits UserIdAndToken,SellerIdAndToken,RequestSignUp{

    }
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static non-sealed class UserIdAndToken implements KakaoLoginResponse {
        private Long memberId;
        private String accessToken;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static non-sealed class SellerIdAndToken implements KakaoLoginResponse {
        private Long memberId;
        private Long sellerId;
        private String accessToken;
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
