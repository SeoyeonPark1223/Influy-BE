package com.influy.domain.faqCategory.repository;

import com.influy.domain.faqCategory.entity.FaqCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqCategoryRepository extends JpaRepository<FaqCategory, Long> {
    Page<FaqCategory> findAllByItemId(Long itemId, Pageable pageable);
    boolean existsByItemIdAndCategory(Long itemId, String category);
}
