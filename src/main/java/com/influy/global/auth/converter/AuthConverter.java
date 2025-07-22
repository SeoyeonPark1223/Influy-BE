package com.influy.global.auth.converter;

import com.influy.global.auth.dto.AuthResponseDTO;

public class AuthConverter {
    public static AuthResponseDTO.UserIdAndToken toIdAndTokenDto(Long memberId, String accessToken) {

        return AuthResponseDTO.UserIdAndToken.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .build();
    }

    public static AuthResponseDTO.SellerIdAndToken toSellerIdAndToken(Long memberId, Long sellerId, String accessToken) {

        return AuthResponseDTO.SellerIdAndToken.builder()
                .memberId(memberId)
                .sellerId(sellerId)
                .accessToken(accessToken)
                .build();
    }

    public static AuthResponseDTO.RequestSignUp toRequestSignUp(Long kakaoId) {
        return AuthResponseDTO.RequestSignUp.builder()
                .message("회원가입이 필요합니다.")
                .kakaoId(kakaoId)
                .build();
    }
}
