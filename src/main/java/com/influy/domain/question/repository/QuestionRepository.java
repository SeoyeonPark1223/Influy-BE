package com.influy.domain.question.repository;

import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("""
    SELECT q
    FROM Question q
    JOIN q.questionTag qt
    WHERE qt.questionCategory = :questionCategory
    AND q.isAnswered = :isAnswered
    """)
    Page<Question> findAllByQuestionCategoryAndIsAnswered(QuestionCategory questionCategory, Boolean isAnswered, Pageable pageable);

    @Query("""
        SELECT q FROM Question q
        WHERE q.id = :questionId
          AND q.questionTag.id = :questionTagId
          AND q.questionTag.questionCategory.id = :questionCategoryId
          AND q.questionTag.questionCategory.item.id = :itemId
    """)
    Optional<Question> findValidQuestion(Long itemId, Long questionCategoryId, Long questionTagId, Long questionId);
}
