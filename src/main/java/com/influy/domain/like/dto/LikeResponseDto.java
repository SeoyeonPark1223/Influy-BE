package com.influy.domain.like.dto;

import com.influy.domain.like.entity.TargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class LikeResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddSellerLikeDto {
        @Schema(description = "멤버 id", example = "1")
        private Long memberId;

        @Schema(description = "셀러/아이템 찜", example = "셀러")
        private TargetType targetType;

        @Schema(description = "셀러 id", example = "1")
        private Long sellerId;

        @Schema(description = "셀러 닉네임", example = "뾰로롱")
        private String sellerName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddItemLikeDto {
        @Schema(description = "멤버 id", example = "1")
        private Long memberId;

        @Schema(description = "셀러/아이템 찜", example = "아이템")
        private TargetType targetType;

        @Schema(description = "아이템 id", example = "1")
        private Long itemId;

        @Schema(description = "아이템 이름", example = "빤짝 원피스")
        private String itemName;
    }
}
