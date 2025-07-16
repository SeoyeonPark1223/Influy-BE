package com.influy.domain.member.entity;

import lombok.Getter;

@Getter
public enum MemberRole {
    USER(Authority.USER),
    SELLER(Authority.SELLER),
    MANAGER(Authority.MANAGER),
    ADMIN(Authority.ADMIN);

    private final String authority;

    MemberRole(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String SELLER = "ROLE_SELLER";
        public static final String MANAGER = "ROLE_MANAGER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
