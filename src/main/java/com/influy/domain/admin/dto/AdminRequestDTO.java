package com.influy.domain.admin.dto;

import com.influy.domain.member.dto.MemberRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.List;

public class AdminRequestDTO {

    @Getter
    public static class AdminJoin{
        @Schema(description = "유저네임", example = "")
        private String username;
        @Schema(description = "멤버의 카카오 회원 번호", example = "1234567890")
        private Long kakaoId;
    }

    @Getter
    public static class CopyItemToSeller{
        @Schema(description = "상품 인가할 셀러 아이디 리스트", example = "1")
        private Long sellerId;
        @Schema(description = "인가할 상품 아이디 리스트", example = "[1,2]")
        private List<Long> itemIds;
    }
}
