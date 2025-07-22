package com.influy.domain.answer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

public class AnswerRequestDto {
    @Getter
    public static class AnswerIndividualDto {
        @Schema(description = "답변 내용", example = "이건 이거임당")
        private String answerContent;
    }

    @Getter
    public static class AnswerCommonDto {
        @Schema(description = "답변할 질문 id 리스트", example = "[1, 2]")
        private List<Long> questionIdList;

        @Schema(description = "답변 내용", example = "이건 이거임당")
        private String answerContent;
    }

    @Getter
    public static class DeleteDto {
        @Schema(description = "삭제할 질문 id 리스트 (1개도 가능)", example = "[1, 2]")
        private List<Long> questionIdList;
    }
}
