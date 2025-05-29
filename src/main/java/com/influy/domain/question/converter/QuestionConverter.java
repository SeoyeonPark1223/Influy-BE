package com.influy.domain.question.converter;

import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.entity.Question;

public class QuestionConverter {
    public static QuestionResponseDTO.General toGeneralDTO(Question question) {
        return QuestionResponseDTO.General.builder()
                .id(question.getId())
                .content(question.getContent())
                .nickname(question.getUser().getNickname())
                .itemPeriod(question.getItemPeriod())
                .createdAt(question.getCreatedAt())
                .build();
    }
}
