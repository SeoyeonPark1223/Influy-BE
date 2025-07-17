package com.influy.domain.ai.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AiRequestDTO {
    @Builder @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionTag{
        private Long id;
        private String name;
    }
    @Builder @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Question{
        private Long id;
        private String content;
    }


}
