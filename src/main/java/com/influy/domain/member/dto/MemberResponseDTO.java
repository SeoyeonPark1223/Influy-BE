package com.influy.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

public class MemberResponseDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberProfile{
        @Schema(description = "멤버 DB id", example = "1")
        private Long id;

        @Schema(description = "멤버가 지정한 아이디", example = "@crazy_dog")
        private String username;

        @Schema(description = "멤버 닉네임", example = "이민용")
        private String nickname;

        @Schema(description = "프로필 이미지 url", example = "https://amazon.~")
        private String profileImg;

        @Schema(description = "멤버 가입 일자", example = "2025-07-06T15:54:43.186Z")
        private LocalDateTime createdAt;
    }

}
