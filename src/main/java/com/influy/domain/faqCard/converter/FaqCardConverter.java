package com.influy.domain.faqCard.converter;

import com.influy.domain.faqCard.dto.FaqCardResponseDto;
import com.influy.domain.faqCard.entity.FaqCard;
import org.springframework.data.domain.Page;

import java.util.List;

public class FaqCardConverter {
    public static FaqCardResponseDto.QuestionCardDto toQuestionCardDto(FaqCard questionCard) {
        return FaqCardResponseDto.QuestionCardDto.builder()
                .id(questionCard.getId())
                .questionContent(questionCard.getQuestionContent())
                .isPinned(questionCard.getIsPinned())
                .build();
    }

    public static FaqCardResponseDto.PageDto toPageDto(Page<FaqCard> questionCardPage) {
        List<FaqCardResponseDto.QuestionCardDto> questionCardDtoList = questionCardPage.stream()
                .map(FaqCardConverter::toQuestionCardDto).toList();

        return FaqCardResponseDto.PageDto.builder()
                .questionCardList(questionCardDtoList)
                .listSize(questionCardPage.getContent().size())
                .totalPage(questionCardPage.getTotalPages())
                .totalElements(questionCardPage.getTotalElements())
                .isFirst(questionCardPage.isFirst())
                .isLast(questionCardPage.isLast())
                .build();
    }
}

