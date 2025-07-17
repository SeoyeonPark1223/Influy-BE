package com.influy.domain.sellerProfile.dto;

import com.influy.domain.sellerProfile.entity.ItemSortType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class SellerProfileResponseDTO {

    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SellerProfile{
        @Schema(description = "셀러의 회원 번호")
        private Long id;
        @Schema(description = "셀러의 셀러 프로필 id(마켓 id)")
        private Long sellerId;
        private String nickname;
        private String backgroundImg;
        private String profileImg;
        private Boolean isPublic;
        private ItemSortType itemSortType;
        private String instagram;
        private String tiktok;
        private String youtube;
        private String email;
    }

    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SortType{
        private ItemSortType itemSortType;
    }
}
