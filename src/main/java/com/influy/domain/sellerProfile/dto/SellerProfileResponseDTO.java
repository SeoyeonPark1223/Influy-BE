package com.influy.domain.sellerProfile.dto;

import com.influy.domain.sellerProfile.entity.ItemSortType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class SellerProfileResponseDTO {

    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MarketProfile {
        private SellerProfile sellerProfile;
        @Schema(description = "아이템 정렬 방식", example = "END_DATE")
        private ItemSortType itemSortType;
        @Schema(description = "마켓 공개 여부", example = "true")
        private Boolean isPublic;
        @Schema(description = "로그인한 사용자가 해당 셀러를 찜했는지", example = "true")
        private Boolean isLiked;
        @Schema(description = "공개 상품 수", example = "24")
        private Long publicItemCnt;
        @Schema(description = "보관 상품 수", example = "9")
        private Long privateItemCnt;
        @Schema(description = "리뷰 수", example = "130")
        private Long reviews;
    }

    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SellerProfile{
        @Schema(description = "셀러의 회원 번호", example = "1")
        private Long id;
        @Schema(description = "셀러의 셀러 프로필 id(마켓 id)", example = "2")
        private Long sellerId;
        @Schema(description = "셀러의 유저네임", example = "@god")
        private String username;
        @Schema(description = "셀러의 닉네임", example = "윤계상")
        private String nickname;
        @Schema(description = "배경사진", example = "https://amazon~")
        private String backgroundImg;
        @Schema(description = "프로필 사진", example = "https://amazon~")
        private String profileImg;
        @Schema(description = "인스타그램", example = "https://(www.)instagram.com/계정(?asda~)")
        private String instagram;
        @Schema(description = "틱톡", example = "제약 X")
        private String tiktok;
        @Schema(description = "유튜브", example = "제약 X")
        private String youtube;
        @Schema(description = "이메일", example = "제약 X")
        private String email;
    }



    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SortType{
        @Schema(description = "아이템 정렬 타입", example = "END_DATE")
        private ItemSortType itemSortType;
    }
}
