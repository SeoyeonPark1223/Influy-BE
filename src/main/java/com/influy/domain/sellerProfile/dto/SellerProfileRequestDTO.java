package com.influy.domain.sellerProfile.dto;

import lombok.Getter;

public class SellerProfileRequestDTO {
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
