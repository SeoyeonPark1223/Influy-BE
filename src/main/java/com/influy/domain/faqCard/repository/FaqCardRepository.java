package com.influy.domain.faqCard.repository;

import com.influy.domain.faqCard.entity.FaqCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqCardRepository extends JpaRepository<FaqCard, Long> {
    Page<FaqCard> findByFaqCategoryId(Long faqCategoryId, Pageable pageable);
}
