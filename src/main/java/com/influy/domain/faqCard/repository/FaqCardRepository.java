package com.influy.domain.faqCard.repository;

import com.influy.domain.faqCard.dto.jpql.FaqCardJPQLResult;
import com.influy.domain.faqCard.entity.FaqCard;
import com.influy.domain.faqCategory.entity.FaqCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FaqCardRepository extends JpaRepository<FaqCard, Long> {
    Page<FaqCard> findByFaqCategoryId(Long faqCategoryId, Pageable pageable);
    void deleteAllByFaqCategory(FaqCategory faqCategory);

    @Query("""
    SELECT fc.faqCategory.id AS faqCategoryId, fc AS faqCard
    FROM FaqCard fc
    WHERE fc.faqCategory.id IN (:ids)
    """)
    List<FaqCardJPQLResult.WithCategoryId> findAllByFaqCategoryIds(@Param("ids")List<Long> faqCategoryIds);
}
