package com.influy.domain.questionTag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QuestionTagResponseDTO {

    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class General{
        @Schema(description = "태그 아이디", example = "2")
        private Long id;
        @Schema(description = "태그 이름", example = "예약 변경")
        private String name;
        @Schema(description = "해당 태그에 포함된 질문 수", example = "24")
        private Integer questionCount;
    }
}
