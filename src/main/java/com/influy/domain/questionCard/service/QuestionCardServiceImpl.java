package com.influy.domain.questionCard.service;

import com.influy.domain.questionCard.entity.QuestionCard;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionCardServiceImpl implements QuestionCardService {
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionCard> getPage(Long sellerId, Long itemId, Long faqCategoryId, Integer pageNumber) {
        return null;
    }
}
