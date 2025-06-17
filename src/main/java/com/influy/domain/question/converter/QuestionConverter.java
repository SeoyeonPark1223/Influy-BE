package com.influy.domain.question.converter;

import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDTO;

import java.sql.Timestamp;

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

    //native sql 전용 converter
    public static QuestionResponseDTO.General toGeneralDTO(Object[] row, String nickname) {
        return QuestionResponseDTO.General.builder()
                .id(((Number) row[0]).longValue())
                .nickname(nickname)
                .content((String) row[2])
                .createdAt(((Timestamp) row[3]).toLocalDateTime())
                .itemPeriod((Integer) row[4])
                .build();
    }
}
