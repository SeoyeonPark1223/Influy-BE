package com.influy.domain.questionTag.converter;

import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionTag.dto.QuestionTagResponseDTO;
import com.influy.domain.questionTag.entity.QuestionTag;

import java.util.List;

public class QuestionTagConverter {

    public static QuestionTag toQuestionTag(String name, QuestionCategory category){
        return QuestionTag.builder()
                .name(name)
                .questionCategory(category)
                .build();
    }

    public static QuestionTagResponseDTO.General toGeneralDTO(QuestionTag questionTag) {
        return QuestionTagResponseDTO.General.builder()
                .id(questionTag.getId())
                .name(questionTag.getName())
                .questionCount(questionTag.getQuestionList().size())
                .build();
    }
}
