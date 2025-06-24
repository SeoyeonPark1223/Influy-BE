package com.influy.domain.member.dto;

import com.influy.domain.sellerProfile.entity.ItemSortType;
import lombok.*;

import java.time.LocalDateTime;

public class MemberResponseDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberProfile{
        private Long id;
        private String username;
        private String nickname;
        private String name;
        private String profileImg;
        private LocalDateTime createdAt;
    }
}
