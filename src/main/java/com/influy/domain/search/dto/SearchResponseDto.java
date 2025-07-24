package com.influy.domain.search.dto;

import com.influy.domain.item.dto.ItemResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class SearchResponseDto {
    public static class SearchResultDto {
        @Schema(description = "셀러 리스트")
        private List<SellerPageResultDto> sellerPageDtoList;

        @Schema(description = "아이템 리스트")
        private List<ItemResponseDto.HomeItemViewPageDto> itemPageDtoList;
    }

    public static class SellerPageResultDto {
        @Schema(description = "셀러 리스트")
        private List<SellerResultDto> sellerDtoList;

        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }
}
