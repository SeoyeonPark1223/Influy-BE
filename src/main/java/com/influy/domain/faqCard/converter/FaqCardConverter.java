package com.influy.domain.faqCard.converter;

import com.influy.domain.faqCard.dto.FaqCardRequestDto;
import com.influy.domain.faqCard.dto.FaqCardResponseDto;
import com.influy.domain.faqCard.entity.FaqCard;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;

import java.util.List;

public class FaqCardConverter {
    public static FaqCard toFaqCard(FaqCardRequestDto.CreateDto request, FaqCategory faqCategory, SellerProfile seller) {
        return FaqCard.builder()
                .faqCategory(faqCategory)
                .seller(seller)
                .questionContent(request.getQuestionContent())
                .answerContent(request.getAnswerContent())
                .backgroundImageLink(request.getBackgroundImgLink())
                .build();
    }

    public static FaqCardResponseDto.QuestionCardDto toQuestionCardDto(FaqCard questionCard) {
        return FaqCardResponseDto.QuestionCardDto.builder()
                .id(questionCard.getId())
                .questionContent(questionCard.getQuestionContent())
                .pinned(questionCard.getIsPinned())
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

    public static FaqCardResponseDto.CreateResultDto toCreateResultDto(FaqCard questionCard) {
        return FaqCardResponseDto.CreateResultDto.builder()
                .id(questionCard.getId())
                .questionContent(questionCard.getQuestionContent())
                .build();
    }

    public static FaqCardResponseDto.AnswerCardDto toAnswerCardDto(FaqCard faqCard) {
        return FaqCardResponseDto.AnswerCardDto.builder()
                .id(faqCard.getId())
                .questionContent(faqCard.getQuestionContent())
                .pinned(faqCard.getIsPinned())
                .answerContent(nonNull(faqCard.getAnswerContent()))
                .faqCategory(faqCard.getFaqCategory().getCategory())
                .createdAt(faqCard.getCreatedAt())
                .backgroundImgLink(nonNull(faqCard.getBackgroundImageLink()))
                .build();
    }

    public static FaqCardResponseDto.UpdateResultDto toUpdateResultDto(FaqCard faqCard) {
        return FaqCardResponseDto.UpdateResultDto.builder()
                .id(faqCard.getId())
                .questionContent(faqCard.getQuestionContent())
                .answerContent(nonNull(faqCard.getAnswerContent()))
                .backgroundImgLink(nonNull(faqCard.getBackgroundImageLink()))
                .build();
    }

    public static FaqCardResponseDto.DeleteResultDto toDeleteResultDto(Long faqCardId) {
        return FaqCardResponseDto.DeleteResultDto.builder()
                .id(faqCardId)
                .build();
    }

    private static String nonNull(String value) {
        return value != null ? value : "";
    }
}

