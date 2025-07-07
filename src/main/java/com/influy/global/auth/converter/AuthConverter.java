package com.influy.global.auth.converter;

import com.influy.domain.member.entity.Member;
import com.influy.global.auth.dto.AuthResponseDTO;

public class AuthConverter {
    public static AuthResponseDTO.TokenPair toTokenPair(Long memberId, String accessToken, String refreshToken) {

        return AuthResponseDTO.TokenPair.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static AuthResponseDTO.RequestSignUp toRequestSignUp(Long kakaoId) {
        return AuthResponseDTO.RequestSignUp.builder()
                .message("회원가입이 필요합니다.")
                .kakaoId(kakaoId)
                .build();
    }
}
