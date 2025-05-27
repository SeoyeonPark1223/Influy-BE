package com.influy.domain.questionCard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class QuestionCardResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionCardDto {
        @Schema(description = "질문 카드 id", example = "1")
        private Long id;

        @Schema(description = "질문 내용", example = "구성이 어떻게 되나요?")
        private String content;

        @Schema(description = "고정 여부", example = "true")
        private boolean isPinned;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageDto {
        @Schema(description = "질문 카드 리스트")
        private List<QuestionCardDto> questionCardList;

        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }
}
