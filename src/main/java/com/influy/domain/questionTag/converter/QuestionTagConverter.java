package com.influy.domain.questionTag.converter;

import com.influy.domain.questionTag.dto.QuestionTagResponseDTO;
import com.influy.domain.questionTag.entity.QuestionTag;

public class QuestionTagConverter {

    public static QuestionTagResponseDTO.General toGeneralDTO(QuestionTag questionTag) {
        return QuestionTagResponseDTO.General.builder()
                .id(questionTag.getId())
                .name(questionTag.getName())
                .questionCount(questionTag.getQuestionList().size())
                .build();
    }
}
