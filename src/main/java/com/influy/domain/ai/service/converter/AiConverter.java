package com.influy.domain.ai.service.converter;

import com.influy.domain.ai.service.dto.AiRequestDTO;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionTag.entity.QuestionTag;

public class AiConverter {
    public static AiRequestDTO.QuestionTag toAiQuestionTagDTO(QuestionTag questionTag) {
        return AiRequestDTO.QuestionTag.builder()
                .id(questionTag.getId())
                .name(questionTag.getName())
                .build();
    }

    public static AiRequestDTO.Question toAiQuestionDTO(Question question) {

        return AiRequestDTO.Question.builder()
                .id(question.getId())
                .content(question.getContent())
                .build();
    }
}
