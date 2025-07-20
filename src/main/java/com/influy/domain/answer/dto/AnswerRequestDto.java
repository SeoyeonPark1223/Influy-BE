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
        @Schema(description = "답변할 질문 id 리스트")
        private List<Long> questionIdList;

        @Schema(description = "답변 내용", example = "이건 이거임당")
        private String answerContent;
    }

    @Getter
    public static class DeleteDto {
        @Schema(description = "삭제할 질문 id 리스트 (1개도 가능)")
        private List<Long> questionIdList;
    }

    @Getter
    public static class QuestionToFaqDto {
        @Schema(description = "FAQ 카테고리 id", example = "1")
        private Long faqCategoryId;

        @Schema(description = "FAQ 질문 내용", example = "궁그매용")
        private String questionContent;

        @Schema(description = "FAQ 답변 내용", example = "이건 이거입니당")
        private String answerContent;

        @Schema(description = "배경 이미지 링크", example = "xxxxx.png")
        private String backgroundImgLink;

        @Schema(description = "고정하기 여부", example = "true")
        private boolean pinned;

        @Schema(description = "사진 비율 (false: 꽉 채운거, true: 높이 비율로 조정)", example = "false")
        private boolean adjustImg;
    }
}
