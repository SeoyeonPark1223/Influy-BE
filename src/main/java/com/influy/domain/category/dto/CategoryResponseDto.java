package com.influy.domain.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CategoryResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDto {
        @Schema(description = "카테고리 id", example = "1")
        private Long id;

        @Schema(description = "카테고리 이름", example = "사이즈")
        private String name;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryListDto {
        @Schema(description = "카테고리 리스트")
        private List<CategoryDto> categoryDtoList;
    }
}
