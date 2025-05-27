package com.influy.domain.questionCard.service;

import com.influy.domain.questionCard.entity.QuestionCard;
import org.springframework.data.domain.Page;

public interface QuestionCardService {
    Page<QuestionCard> getPage(Long sellerId, Long itemId, Long faqCategoryId, Integer pageNumber);
}
