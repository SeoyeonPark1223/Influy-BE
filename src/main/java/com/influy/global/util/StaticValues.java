package com.influy.global.util;

import java.util.Map;
import java.util.Set;

public class StaticValues {
    public static final long ACCESS_TOKEN_EXPIRE = 1000 * 60 * 60 * 24* 7; // 1주
    public static final long REFRESH_EXPIRE = 1000L * 60 * 60 * 24 * 14; // 2주
    // 경로 + 허용 메서드 조합 정의
    public static final String[] WHITE_LIST = {
            "/member/register",
            "member/reissue",
            "/swagger-ui/**",
            "/api-docs/**",
            "/api-docs/json/swagger-config",
            "/api-docs/json",
            "/favicon.ico",
            "/"

    };
}
