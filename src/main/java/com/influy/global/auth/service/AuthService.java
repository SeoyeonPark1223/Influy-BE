package com.influy.global.auth.service;


import com.influy.domain.member.entity.Member;
import com.influy.global.auth.TokenPair;
import com.influy.global.auth.dto.AuthRequestDTO;
import com.influy.global.auth.dto.AuthResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponseDTO.KakaoLoginResponse SocialLogIn(String code, HttpServletResponse response);

    Long GetSocialUserId(String accessToken);

    TokenPair issueToken(Member member);

    TokenPair reissueToken(HttpServletRequest request, HttpServletResponse response);

    void signOut(HttpServletRequest request, Member member);

    AuthRequestDTO.KakaoUserProfile getUserProfile(Long socialUserId);
}
