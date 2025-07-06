package com.influy.global.auth.dto;

import lombok.*;

public class AuthResponseDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TokenPair {
        private Long memberId;
        private String accessToken;
        private String refreshToken;
    }
}
