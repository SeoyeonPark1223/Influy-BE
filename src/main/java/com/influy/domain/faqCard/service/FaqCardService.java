package com.influy.domain.faqCard.service;

import com.influy.domain.answer.dto.AnswerRequestDto;
import com.influy.domain.faqCard.dto.FaqCardRequestDto;
import com.influy.domain.faqCard.entity.FaqCard;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;
import org.springframework.data.domain.Page;

public interface FaqCardService {
    Page<FaqCard> getFaqCardPage(Long sellerId, Long itemId, Long faqCategoryId, PageRequestDto pageRequest);
    FaqCard create(CustomUserDetails userDetails, Long itemId, Long faqCategoryId, FaqCardRequestDto.CreateDto request);
    FaqCard getAnswerCard(Long sellerId, Long itemId, Long faqCardId);
    FaqCard update(CustomUserDetails userDetails, Long itemId, Long faqCardId, FaqCardRequestDto.UpdateDto request);
    FaqCard pinUpdate(CustomUserDetails userDetails, Long itemId, Long faqCardId, boolean isPinned);
    void delete(CustomUserDetails userDetails, Long itemId, Long faqCardId);
    FaqCard questionToFaq(SellerProfile seller, AnswerRequestDto.QuestionToFaqDto request);
}

