package com.influy.domain.questionCard.repository;

import com.influy.domain.questionCard.entity.QuestionCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionCardRepository extends JpaRepository<QuestionCard, Long> {
    Page<QuestionCard> findByFaqCategoryId(Long faqCategoryId, Pageable pageable);
}
