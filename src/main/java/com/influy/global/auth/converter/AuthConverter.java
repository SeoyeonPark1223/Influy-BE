package com.influy.global.auth.converter;

import com.influy.global.auth.dto.AuthResponseDTO;

public class AuthConverter {
    public static AuthResponseDTO.IdAndToken toTokenPair(Long memberId, String accessToken) {

        return AuthResponseDTO.IdAndToken.builder()
                .memberId(memberId)
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
