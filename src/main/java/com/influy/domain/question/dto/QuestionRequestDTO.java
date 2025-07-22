package com.influy.domain.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class QuestionRequestDTO {

    @Getter
    public static class Create{
        @Schema(description = "질문 내용", example = "폐업하시면 저는 옷 어디서 사나요?")
        private String content;
    }
}
