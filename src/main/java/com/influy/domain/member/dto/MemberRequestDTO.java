package com.influy.domain.member.dto;

import com.influy.domain.member.entity.MemberRole;
import lombok.Getter;

public class MemberRequestDTO {
    @Getter
    public static class UserJoin {
        private String username;
        private Long kakaoId;
        private String password;
        private String name;
        private String nickname;
        private MemberRole role;
    }
    @Getter
    public static class SellerJoin {
        private UserJoin userInfo;
        private String email;
        private String instagram;
    }

}
