package com.influy.domain.questionCategory.converter;

import com.influy.domain.item.entity.Item;
import com.influy.domain.question.dto.JPQLQuestionDTO;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDTO;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDTO;
import com.influy.domain.questionCategory.entity.QuestionCategory;

import java.sql.Timestamp;
import java.util.List;

public class QuestionCategoryConverter {

    public static QuestionCategory toEntity(Item item, QuestionCategoryRequestDTO.Create request) {
        return QuestionCategory.builder()
                .category(request.getName())
                .item(item)
                .build();
    }

    public static QuestionCategoryResponseDTO.General toGeneralDTO(QuestionCategory category,
                                                                   Integer pending,
                                                                   Integer answered) {
        return QuestionCategoryResponseDTO.General.builder()
                .id(category.getId())
                .name(category.getCategory())
                .pendingCnt(pending)
                .answeredCnt(answered)
                .build();
    }

    //JPQL result to Preview
    public static QuestionCategoryResponseDTO.Preview toPreviewDTO(JPQLQuestionDTO result) {
        return QuestionCategoryResponseDTO.Preview.builder()
                .id(result.getCategoryId())
                .name(result.getCategoryName())
                .answeredCnt(0)
                .pendingCnt(0)
                .build();
    }


}
