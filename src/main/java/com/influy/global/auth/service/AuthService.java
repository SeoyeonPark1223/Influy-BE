package com.influy.global.auth.service;


import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.global.auth.dto.AuthRequestDTO;
import com.influy.global.auth.dto.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO.KakaoLoginResponse kakaoSignIn(String code);

    Long getKakaoUserID(String accessToken);

    AuthResponseDTO.TokenPair issueToken(Member member);
}
