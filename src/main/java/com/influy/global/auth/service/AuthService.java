package com.influy.global.auth.service;


import com.influy.global.auth.dto.AuthRequestDTO;
import com.influy.global.auth.dto.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO.TokenPair kakaoSignIn(String code);

    Long getKakaoUserID(String accessToken);
}
