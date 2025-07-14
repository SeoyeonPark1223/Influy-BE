package com.influy.global.auth.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import lombok.Getter;

public class AuthRequestDTO {
    @Getter
    public static class KakaoToken{
        private String access_token;
    }

    @Getter
    public static class KakaoUser{
        private String id;

    }

}
