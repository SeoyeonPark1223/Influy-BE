package com.influy.domain.answer.repository;

import com.influy.domain.answer.entity.Answer;
import com.influy.domain.answer.entity.AnswerType;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionTag.entity.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("""
        SELECT a FROM Answer a
        WHERE a.question.questionTag.id = :questionTagId
          AND (a.answerType = 'INDIVIDUAL' OR a.answerType = 'FAQ')
    """)
    List<Answer> findIndividualAndFaqAnswersByQuestionTagId(Long questionTagId);

    @Query("""
    SELECT a FROM Answer a
    WHERE a.question.questionTag.id = :questionTagId 
    AND a.answerType = 'COMMON'
    """)
    List<Answer> findCommonAnswersByQuestionTagId(Long questionTagId);
}
