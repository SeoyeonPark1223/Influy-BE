package com.influy.domain.faqCard.converter;

import com.influy.domain.answer.dto.AnswerRequestDto;
import com.influy.domain.faqCard.dto.FaqCardRequestDto;
import com.influy.domain.faqCard.dto.FaqCardResponseDto;
import com.influy.domain.faqCard.entity.FaqCard;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.item.entity.Item;
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
                .isPinned(request.isPinned())
                .adjustImg(request.isAdjustImg())
                .build();
    }

    public static FaqCard toFaqCard(AnswerRequestDto.QuestionToFaqDto request, FaqCategory faqCategory, SellerProfile seller) {
        return FaqCard.builder()
                .faqCategory(faqCategory)
                .seller(seller)
                .questionContent(request.getQuestionContent())
                .answerContent(request.getAnswerContent())
                .backgroundImageLink(request.getBackgroundImgLink())
                .isPinned(request.isPinned())
                .adjustImg(request.isAdjustImg())
                .build();
    }

    public static FaqCardResponseDto.QuestionCardDto toQuestionCardDto(FaqCard questionCard) {
        return FaqCardResponseDto.QuestionCardDto.builder()
                .id(questionCard.getId())
                .questionContent(questionCard.getQuestionContent())
                .pinned(questionCard.getIsPinned())
                .updatedAt(questionCard.getUpdatedAt())
                .build();
    }

    public static FaqCardResponseDto.QuestionCardPageDto toQuestionCardPageDto(Page<FaqCard> questionCardPage) {
        List<FaqCardResponseDto.QuestionCardDto> questionCardDtoList = questionCardPage.stream()
                .map(FaqCardConverter::toQuestionCardDto).toList();

        return FaqCardResponseDto.QuestionCardPageDto.builder()
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

    public static FaqCardResponseDto.FaqCardDto toFaqCardDto(FaqCard faqCard) {
        return FaqCardResponseDto.FaqCardDto.builder()
                .id(faqCard.getId())
                .questionContent(faqCard.getQuestionContent())
                .pinned(faqCard.getIsPinned())
                .adjustImg(faqCard.getAdjustImg())
                .answerContent(nonNull(faqCard.getAnswerContent()))
                .faqCategoryId(faqCard.getFaqCategory().getId())
                .updatedAt(faqCard.getUpdatedAt())
                .backgroundImgLink(nonNull(faqCard.getBackgroundImageLink()))
                .build();
    }

    public static FaqCardResponseDto.FaqCardPageDto toFaqCardPageDto(Page<FaqCard> faqCardPage) {
        List<FaqCardResponseDto.FaqCardDto> faqCardDtoList = faqCardPage.stream()
                .map(FaqCardConverter::toFaqCardDto).toList();

        return FaqCardResponseDto.FaqCardPageDto.builder()
                .faqCardList(faqCardDtoList)
                .listSize(faqCardPage.getContent().size())
                .totalPage(faqCardPage.getTotalPages())
                .totalElements(faqCardPage.getTotalElements())
                .isFirst(faqCardPage.isFirst())
                .isLast(faqCardPage.isLast())
                .build();
    }

    public static FaqCardResponseDto.UpdateResultDto toUpdateResultDto(FaqCard faqCard) {
        return FaqCardResponseDto.UpdateResultDto.builder()
                .id(faqCard.getId())
                .questionContent(faqCard.getQuestionContent())
                .answerContent(nonNull(faqCard.getAnswerContent()))
                .backgroundImgLink(nonNull(faqCard.getBackgroundImageLink()))
                .pinned(faqCard.getIsPinned())
                .faqCategoryId(faqCard.getFaqCategory().getId())
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

