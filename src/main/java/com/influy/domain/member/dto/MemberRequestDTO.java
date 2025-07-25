package com.influy.domain.member.dto;

import com.influy.domain.member.entity.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Null;
import lombok.Getter;

import java.util.List;

public class MemberRequestDTO {
    @Getter
    public static class UserJoin {
        @Schema(description = "멤버 고유 아이디", example = "@rapper_mj")
        private String username;

        @Schema(description = "멤버의 카카오 회원 번호", example = "1234567890")
        private Long kakaoId;

        @Schema(description = "관심 카테고리 id 리스트", example = "[1,2,5,9]")
        private List<Long> interestedCategories;

    }
    @Getter
    public static class SellerJoin {
        private UserJoin userInfo;
        @Schema(description = "이메일", example = "influy@naver.com")
        private String email;
        @Schema(description = "인스타그램", example = "https://www.instagram.com/계정?랜덤문자열")
        private String instagram;
        @Nullable
        @Schema(description = "틱톡", example = "서버단 제약은 없음")
        private String tiktok;
        @Nullable
        @Schema(description = "유튜브", example = "서버단 제약은 없음")
        private String youtube;
    }
    @Getter
    public static class UsernameDuplicateCheck{
        @Schema(description = "유저네임", example = "@rapper_mj")
        private String username;
    }

    @Getter
    public static class UpdateProfile {

        @Nullable
        @Schema(description = "닉네임", example = "민정이")
        private String nickname;

        @Nullable
        @Schema(description = "프로필 사진", example = "http://amazon.s3...")
        private String profileUrl;
    }

    @Getter
    public static class UpdateUsername{
        @Schema(description = "유저네임(id)", example = "@rapper_mj")
        private String username;
    }

}
