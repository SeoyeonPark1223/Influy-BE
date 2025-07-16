package com.influy.global.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

public class AuthRequestDTO {
    @Getter
    public static class KakaoToken{
        private String access_token;
    }

    @Getter
    public static class KakaoUser{
        private String id;

    }

    @Getter @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoUserProfile {
        private KakaoAccount kakao_account;

        @Getter @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class KakaoAccount {
            private Profile profile;

            @Getter @Setter
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Profile {
                private String nickname;
            }
        }
    }


}
