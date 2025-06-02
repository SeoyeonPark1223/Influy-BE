package com.influy.domain.faqCard.service;

import com.influy.domain.faqCard.dto.FaqCardRequestDto;
import com.influy.domain.faqCard.entity.FaqCard;
import org.springframework.data.domain.Page;

public interface FaqCardService {
    Page<FaqCard> getPage(Long sellerId, Long itemId, Long faqCategoryId, Integer pageNumber);
    FaqCard create(Long sellerId, Long itemId, Long faqCategoryId, FaqCardRequestDto.CreateDto request);
    FaqCard getAnswerCard(Long sellerId, Long itemId, Long faqCardId);
    FaqCard update(Long sellerId, Long itemId, Long faqCardId, FaqCardRequestDto.UpdateDto request);
}
