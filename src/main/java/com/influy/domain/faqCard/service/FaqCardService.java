package com.influy.domain.faqCard.service;

import com.influy.domain.faqCard.entity.FaqCard;
import org.springframework.data.domain.Page;

public interface FaqCardService {
    Page<FaqCard> getPage(Long sellerId, Long itemId, Long faqCategoryId, Integer pageNumber);
}
