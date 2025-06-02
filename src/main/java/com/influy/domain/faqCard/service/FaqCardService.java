package com.influy.domain.faqCard.service;

import com.influy.domain.faqCard.dto.FaqCardRequestDto;
import com.influy.domain.faqCard.entity.FaqCard;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface FaqCardService {
    Page<FaqCard> getPage(Long sellerId, Long itemId, Long faqCategoryId, Integer pageNumber);
    FaqCard create(Long sellerId, Long itemId, Long faqCategoryId, FaqCardRequestDto.CreateDto request);
}
