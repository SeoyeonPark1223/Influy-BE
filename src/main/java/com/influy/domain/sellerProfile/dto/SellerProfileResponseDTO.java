package com.influy.domain.sellerProfile.dto;

import com.influy.domain.sellerProfile.entity.ItemSortType;
import lombok.*;

public class SellerProfileResponseDTO {

    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SellerProfile{
        private Long id;
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
