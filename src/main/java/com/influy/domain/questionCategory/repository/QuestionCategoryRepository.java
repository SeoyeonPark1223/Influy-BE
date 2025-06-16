package com.influy.domain.questionCategory.repository;

import com.influy.domain.item.entity.Item;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {
    Page<QuestionCategory> findAllByItem(Item item, Pageable pageable);
}
