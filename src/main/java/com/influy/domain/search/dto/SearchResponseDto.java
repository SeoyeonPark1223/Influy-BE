package com.influy.domain.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class SearchResponseDto {
    public static class SearchResultDto {
        @Schema(description = "셀러 리스트")
        private List<SellerPageResultDto> sellerPageDtoList;

        @Schema(description = "아이템 리스트")
        private List<ItemPageResultDto> itemPageDtoList;
    }

    public static class SellerPageResultDto {

    }

    public static class ItemPageResultDto {
        @Schema(description = "아이템 리스트")
        private <HomeItemViewPageDto>
    }
}
