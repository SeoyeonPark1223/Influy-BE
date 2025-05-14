package com.influy.domain.seller.dto;

import lombok.Getter;

public class SellerRequestDTO {
    @Getter
    public static class Join{
        private String name;
        private String nickname;
        private String email;
    }
}
