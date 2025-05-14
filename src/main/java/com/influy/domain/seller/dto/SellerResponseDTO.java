package com.influy.domain.seller.dto;

import com.influy.domain.seller.entity.ItemSortType;
import lombok.*;

public class SellerResponseDTO {

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
}
