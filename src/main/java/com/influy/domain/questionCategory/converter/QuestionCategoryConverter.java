package com.influy.domain.questionCategory.converter;

import com.influy.domain.item.entity.Item;
import com.influy.domain.question.dto.JPQLQuestionDTO;
import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDto;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDto;
import com.influy.domain.questionCategory.entity.QuestionCategory;

public class QuestionCategoryConverter {

    public static QuestionCategory toQuestionCategory(Item item, String category) {
        return QuestionCategory.builder()
                .category(category)
                .item(item)
                .build();
    }

    public static QuestionCategoryResponseDto.ViewDto toViewDto(QuestionCategory questionCategory) {
        return QuestionCategoryResponseDto.ViewDto.builder()
                .id(questionCategory.getId())
                .category(questionCategory.getCategory())
                .build();
    }

    public static QuestionCategoryResponseDto.DeleteResultDto toDeleteResultDto(Long id) {
        return QuestionCategoryResponseDto.DeleteResultDto.builder()
                .id(id)
                .build();
    }

    public static QuestionCategoryResponseDto.General toGeneralDTO(QuestionCategory category,
                                                                   Integer pending,
                                                                   Integer answered) {
        return QuestionCategoryResponseDto.General.builder()
                .id(category.getId())
                .name(category.getCategory())
                .pendingCnt(pending)
                .answeredCnt(answered)
                .build();
    }

    //JPQL result to Preview
    public static QuestionCategoryResponseDto.Preview toPreviewDTO(JPQLQuestionDTO result) {
        return QuestionCategoryResponseDto.Preview.builder()
                .id(result.getCategoryId())
                .name(result.getCategoryName())
                .answeredCnt(0)
                .pendingCnt(0)
                .build();
    }


}
