package com.influy.domain.seller.dto;

import com.influy.domain.seller.entity.ItemSortType;
import lombok.Getter;

public class SellerRequestDTO {
    @Getter
    public static class Join{
        private String nickname;
        private String email;
    }

    @Getter
    public static class UpdateProfile{
        private String nickname;
        private String backgroundImg;
        private String profileImg;
        private Boolean isPublic;
        private String email;
        private String instagram;
        private String tiktok;
        private String youtube;
    }
}
