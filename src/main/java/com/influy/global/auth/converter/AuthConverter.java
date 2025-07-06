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
}
