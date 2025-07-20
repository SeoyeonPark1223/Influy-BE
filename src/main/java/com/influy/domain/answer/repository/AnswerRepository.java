package com.influy.domain.answer.repository;

import com.influy.domain.answer.entity.Answer;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionTag.entity.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("""
    SELECT q FROM Question q
    WHERE q.id = :questionId
      AND q.questionTag.id = :questionTagId
      AND q.questionTag.questionCategory.id = :questionCategoryId
      AND q.questionTag.questionCategory.item.id = :itemId
""")
    List<Answer> findAllByQuestion_QuestionTag(QuestionTag questionTag);
    Optional<Answer> findByQuestion(Question question);
}
