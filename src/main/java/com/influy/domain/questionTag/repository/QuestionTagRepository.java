package com.influy.domain.questionTag.repository;

import com.influy.domain.questionTag.entity.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface QuestionTagRepository extends JpaRepository<QuestionTag, Long> {
    @Query("""
        SELECT qt FROM QuestionTag qt
        WHERE qt.id = :questionTagId
          AND qt.questionCategory.id = :questionCategoryId
          AND qt.questionCategory.item.id = :itemId
    """)
    Optional<QuestionTag> findValidQuestionTag(Long itemId, Long questionCategoryId, Long questionTagId);
}
