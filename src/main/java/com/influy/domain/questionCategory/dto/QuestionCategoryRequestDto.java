package com.influy.domain.questionCategory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

public class QuestionCategoryRequestDto {
    @Getter
    public static class AddListDto{
        @Schema(description = "추가할 질문 카테고리 이름 리스트", example = "[ 사이즈, 색상, 세탁 ]")
        private List<String> categoryList;
    }
}
