package com.influy.domain.faqCategory.repository;

import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface FaqCategoryRepository extends JpaRepository<FaqCategory, Long> {
    Page<FaqCategory> findAllByItemId(Long itemId, Pageable pageable);
    boolean existsByItemIdAndCategory(Long itemId, String category);
    List<FaqCategory> findAllByItemId(Long itemId, Sort sort);
    int countAllByItemId(Long itemId);
    List<FaqCategory> findAllByItem(Item item);
}
