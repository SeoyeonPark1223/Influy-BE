package com.influy.domain.question.dto;

import lombok.Getter;

public class QuestionRequestDTO {

    @Getter
    public static class Create{
        private String content;
    }
}
