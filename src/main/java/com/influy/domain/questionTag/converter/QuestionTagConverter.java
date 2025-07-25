package com.influy.domain.questionTag.converter;

import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionTag.dto.QuestionTagResponseDTO;
import com.influy.domain.questionTag.dto.jpql.TagJPQLResult;
import com.influy.domain.questionTag.entity.QuestionTag;

public class QuestionTagConverter {

    public static QuestionTag toQuestionTag(String name, QuestionCategory category){
        return QuestionTag.builder()
                .name(name)
                .questionCategory(category)
                .build();
    }

    public static QuestionTagResponseDTO.General toGeneralDTO(TagJPQLResult.QuestionTagInfo questionTagInfo) {
        return QuestionTagResponseDTO.General.builder()
                .id(questionTagInfo.getId())
                .name(questionTagInfo.getTagName())
                .totalQuestions(questionTagInfo.getTotalQuestions())
                .uncheckedExists(questionTagInfo.getUncheckedQuestions() > 0)
                .build();
    }

    public static QuestionTagResponseDTO.General toGeneralTotalDTO(Long id, String name, Long totalQuestions, boolean uncheckedQuestions) {
        return QuestionTagResponseDTO.General.builder()
                .id(id)
                .name(name)
                .totalQuestions(totalQuestions)
                .uncheckedExists(uncheckedQuestions)
                .build();
    }
}
