package com.influy.domain.sellerProfile.dto;

import com.influy.domain.member.dto.MemberRequestDTO;
import lombok.Getter;

public class SellerProfileRequestDTO {
    @Getter
    public static class Join{
        private String nickname;
        private String email;
    }

    @Getter
    public static class UpdateProfile{
        private MemberRequestDTO.UpdateProfile profile;
        private String backgroundImg;
        private String email;
        private String instagram;
        private String tiktok;
        private String youtube;
    }
}
