package com.influy.domain.faqCategory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class FaqCategoryResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewDto {
        @Schema(description = "faq 카테고리 id", example = "1")
        private Long id;

        @Schema(description = "faq 카테고리", example = "상품구성")
        private String category;

        @Schema(description = "faq 카테고리 순서", example = "1")
        private Integer categoryOrder;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListDto {
        @Schema(description = "faq 카테고리 리스트")
        private List<ViewDto> viewList;

        private Integer listSize;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteResultDto {
        @Schema(description = "삭제된 faq 카테고리 id", example = "1")
        private Long id;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateOrderResultDto {
        @Schema(description = "업데이트된 faq 카테고리 리스트")
        private List<ViewDto> updatedList;
    }
}
