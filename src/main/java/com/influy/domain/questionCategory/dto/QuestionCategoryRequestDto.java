package com.influy.domain.questionCategory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class QuestionCategoryRequestDto {
    @Getter
    public static class AddDto{
        @Schema(description = "질문 카테고리 이름", example = "사이즈")
        private String category;
    }

    @Getter
    public static class UpdateDto{
        @Schema(description = "카테고리 id", example = "1")
        private Long id;

        @Schema(description = "질문 카테고리 이름", example = "사이즈")
        private String category;
    }

    @Getter
    public static class DeleteDto{
        @Schema(description = "카테고리 id", example = "1")
        private Long id;
    }
}
