package com.influy.domain.questionCard.converter;

import com.influy.domain.faqCategory.converter.FaqCategoryConverter;
import com.influy.domain.questionCard.dto.QuestionCardResponseDto;
import com.influy.domain.questionCard.entity.QuestionCard;
import org.springframework.data.domain.Page;

import java.util.List;

public class QuestionCardConverter {
    public static QuestionCardResponseDto.QuestionCardDto toQuestionCardDto(QuestionCard questionCard) {
        return QuestionCardResponseDto.QuestionCardDto.builder()
                .id(questionCard.getId())
                .content(questionCard.getContent())
                .isPinned(questionCard.getIsPinned())
                .build();
    }

    public static QuestionCardResponseDto.PageDto toPageDto(Page<QuestionCard> questionCardPage) {
        List<QuestionCardResponseDto.QuestionCardDto> questionCardDtoList = questionCardPage.stream()
                .map(QuestionCardConverter::toQuestionCardDto).toList();

        return QuestionCardResponseDto.PageDto.builder()
                .questionCardList(questionCardDtoList)
                .listSize(questionCardPage.getContent().size())
                .totalPage(questionCardPage.getTotalPages())
                .totalElements(questionCardPage.getTotalElements())
                .isFirst(questionCardPage.isFirst())
                .isLast(questionCardPage.isLast())
                .build();
    }
}
