package com.influy.global.auth.dto;

import lombok.*;

public class AuthResponseDTO {

    public sealed interface LoginResponse permits UserIdAndToken,SellerIdAndToken,RequestSignUp{

    }
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static non-sealed class UserIdAndToken implements LoginResponse {
        private Long memberId;
        private String accessToken;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static non-sealed class SellerIdAndToken implements LoginResponse {
        private Long memberId;
        private Long sellerId;
        private String accessToken;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static non-sealed class RequestSignUp implements LoginResponse {
        private String message;
        private Long kakaoId;
    }
}
