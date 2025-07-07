package com.influy.domain.faqCard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FaqCardRequestDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDto {
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateDto {
        @Schema(description = "FAQ 질문 내용", example = "궁그매용")
        private String questionContent;

        @Schema(description = "FAQ 답변 내용", example = "이건 이거입니당")
        private String answerContent;

        @Schema(description = "배경 이미지 링크", example = "xxxxx.png")
        private String backgroundImgLink;

        @Schema(description = "고정하기 여부", example = "true")
        private Boolean pinned;

        @Schema(description = "FAQ 카테고리 id", example = "1")
        private Long faqCategoryId;
    }
}
