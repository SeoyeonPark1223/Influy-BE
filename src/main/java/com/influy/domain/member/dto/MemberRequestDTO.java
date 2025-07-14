package com.influy.domain.member.dto;

import com.influy.domain.member.entity.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class MemberRequestDTO {
    @Getter
    public static class UserJoin {
        @Schema(description = "멤버 고유 아이디", example = "@rapper_mj")
        private String username;

        @Schema(description = "멤버의 카카오 회원 번호", example = "1234567890")
        private Long kakaoId;

        @Schema(description = "실명", example = "서민정")
        private String name;

        @Schema(description = "닉네임", example = "꽈당민정")
        private String nickname;
    }
    @Getter
    public static class SellerJoin {
        private UserJoin userInfo;
        @Schema(description = "이메일", example = "influy@naver.com")
        private String email;
        @Schema(description = "인스타그램", example = "@rapper_mj")
        private String instagram;
    }
    @Getter
    public static class UsernameDuplicateCheck{
        @Schema(description = "유저네임", example = "@rapper_mj")
        private String username;
    }

}
