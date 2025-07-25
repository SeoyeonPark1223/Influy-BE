package com.influy.domain.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SearchResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellerPageResultDto {
        @Schema(description = "셀러 리스트")
        private List<SellerResultDto> sellerDtoList;

        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellerResultDto {
        @Schema(description = "셀러 id", example = "1")
        private Long sellerId;

        @Schema(description = "셀러 프로필사진", example = "https://...")
        private String sellerProfileImg;

        @Schema(description = "셀러 아이디", example = "@xoyeon")
        private String sellerUsername;

        @Schema(description = "셀러 닉네임", example = "소현소현")
        private String sellerNickname;

        @Schema(description = "셀러 찜 여부", example = "true")
        private Boolean liked;
    }
}
