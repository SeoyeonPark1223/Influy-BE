package com.influy.global.util;

public class StaticValues {
    public static final long ACCESS_TOKEN_EXPIRE = 1000 * 60 * 60 * 24* 7; // 1주
    public static final long REFRESH_EXPIRE = 1000L * 60 * 60 * 24 * 14; // 2주
    public static final String[] WHITE_LIST = {"/member/register", "member/auth/reissue"};
}
