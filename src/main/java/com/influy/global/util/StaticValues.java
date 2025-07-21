package com.influy.global.util;

import java.util.List;

public class StaticValues {
    public static final long ACCESS_TOKEN_EXPIRE = 1000 * 60 * 60 * 24* 7; // 1주
    public static final long REFRESH_EXPIRE = 1000L * 60 * 60 * 24 * 14; // 2주
    // 경로 + 허용 메서드 조합 정의
    public static final String[] SHOULD_NOT_FILTER_LIST = {
            "/member/register",
            "/member/reissue",
            "/swagger-ui",
            "/api-docs",
            "/v3/api-docs",
            "/favicon.ico"
    };
    public static final String[] DEFAULT_QUESTION_CATEGORIES = {"진행일정", "결제", "배송", "기타"};

}
