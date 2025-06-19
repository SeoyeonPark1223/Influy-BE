package com.influy.domain.member.dto;

import com.influy.domain.member.entity.MemberRole;
import lombok.Getter;

public class MemberRequestDTO {
    @Getter
    public static class Join{
        private String username;
        private String password;
        private String name;
        private String nickname;
        private MemberRole role;
    }
}
