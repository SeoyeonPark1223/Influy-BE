package com.influy.domain.questionCategory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QuestionCategoryResponseDTO {

    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class General {
        private Long id;
        private String name;
        private Integer pendingCnt;
        private Integer answeredCnt;
    }
}
