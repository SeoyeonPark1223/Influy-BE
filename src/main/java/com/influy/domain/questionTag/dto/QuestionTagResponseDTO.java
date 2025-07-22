package com.influy.domain.questionTag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class QuestionTagResponseDTO {

    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class General{
        private Long id;
        private String name;
        private Integer questionCount;
    }
}
