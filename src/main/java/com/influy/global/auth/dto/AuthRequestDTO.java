package com.influy.global.auth.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import lombok.Getter;

public class AuthRequestDTO {
    @Getter
    public static class KakaoToken{

        private String token_type;

        private String access_token;

        @Nullable
        private String id_token;

        private Integer expires_in;

        private String refresh_token;

        private Integer refresh_token_expires_in;

        @Nullable
        private String scope;
    }

    @Getter
    public static class KakaoUser{
        private String id;

    }

}
