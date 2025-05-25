package com.influy.domain.questionCategory.converter;

import com.influy.domain.item.entity.Item;
import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDTO;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDTO;
import com.influy.domain.questionCategory.entity.QuestionCategory;

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
}
