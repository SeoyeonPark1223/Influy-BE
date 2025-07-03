package com.influy.domain.faqCategory.repository;

import com.influy.domain.faqCategory.entity.FaqCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FaqCategoryRepository extends JpaRepository<FaqCategory, Long> {
    Page<FaqCategory> findAllByItemId(Long itemId, Pageable pageable);
    boolean existsByItemIdAndCategory(Long itemId, String category);
    @Query("SELECT MAX(f.categoryOrder) FROM FaqCategory f")
    Integer findMaxOrder();
    @Modifying
    @Query("UPDATE FaqCategory f SET f.categoryOrder = f.categoryOrder + 1 " +
            "WHERE f.item.id = :itemId AND f.categoryOrder >= :startOrder AND f.categoryOrder < :endOrder")
    void incrementOrdersFrom(@Param("itemId") Long itemId, @Param("startOrder") Integer startOrder, @Param("endOrder") Integer endOrder);
}
