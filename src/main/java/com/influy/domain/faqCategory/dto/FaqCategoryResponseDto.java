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
    public static class AddResultDto {
        @Schema(description = "추가된 faq 카테고리 리스트")
        private List<ViewDto> addList;
    }

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
    public static class PageDto {
        @Schema(description = "faq 카테고리 리스트")
        private List<ViewDto> viewList;

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
    public static class DeleteResultDto {
        @Schema(description = "삭제된 faq 카테고리 id 리스트", example = "[1, 2]")
        private List<Long> idList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateResultDto {
        @Schema(description = "업데이트된 faq 카테고리 리스트", example = "[1, 2]")
        private List<ViewDto> updatedList;
    }
}
