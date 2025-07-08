package com.influy.global.auth.service;


import com.influy.domain.member.entity.Member;
import com.influy.global.auth.dto.AuthResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponseDTO.KakaoLoginResponse SocialLogIn(String code, HttpServletResponse response);

    Long GetSocialUserId(String accessToken);

    String[] issueToken(Member member);

    String[] reissueToken(HttpServletRequest request, HttpServletResponse response);
}
