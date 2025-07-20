package com.influy.domain.answer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class AnswerRequestDto {
    @Getter
    public static class AnswerIndividualDto {
        @Schema(description = "답변 내용", example = "이건 이거임당")
        private String answerContent;
    }
}
