package com.influy.domain.sellerProfile.dto;

import com.influy.domain.member.dto.MemberRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class SellerProfileRequestDTO {

    @Getter
    public static class UpdateProfile{
        private MemberRequestDTO.UpdateProfile profile;
        @Schema(description = "배경 사진", example = "https://amazon~")
        private String backgroundImg;
        @Schema(description = "이메일", example = "제약X")
        private String email;
        @Schema(description = "인스타", example = "https://(www.)instagram.com/계정(?ㅁㄴㅇㅁㄴ)")
        private String instagram;
        @Schema(description = "틱톡", example = "제약X")
        private String tiktok;
        @Schema(description = "유튜브", example = "제약X")
        private String youtube;
    }
}
