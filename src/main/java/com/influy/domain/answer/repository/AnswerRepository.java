package com.influy.domain.answer.repository;

import com.influy.domain.answer.dto.jpql.AnswerJPQLResult;
import com.influy.domain.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("""
        SELECT a FROM Answer a
        WHERE a.question.questionTag.id = :questionTagId
          AND (a.answerType = 'INDIVIDUAL' OR a.answerType = 'FAQ')
    """)
    List<Answer> findIndividualAndFaqAnswersByQuestionTagId(@Param("questionTagId") Long questionTagId);

    @Query("""
    SELECT a FROM Answer a
    WHERE a.question.questionTag.id = :questionTagId
    AND a.answerType = 'COMMON'
    """)
    List<Answer> findCommonAnswersByQuestionTagId(@Param("questionTagId") Long questionTagId);


    @Query("""
    SELECT q.item.id AS itemId,
           SUM(CASE WHEN a.isChecked = false THEN 1 ELSE 0 END) AS uncheckedCount
    FROM Question q
    LEFT JOIN Answer a ON a.question = q
    WHERE q.member.id = :memberId
    GROUP BY q.item.id
    """)
    List<AnswerJPQLResult.UncheckedAnswer> findAllUnCheckedOfMemberQuestion(@Param("memberId") Long memberId);

    @Modifying
    @Query("UPDATE Answer a SET a.isChecked = true WHERE a.id IN :answerIds AND a.isChecked = false")
    void setAnswersAsChecked(@Param("answerIds") List<Long> answerIds);
}
