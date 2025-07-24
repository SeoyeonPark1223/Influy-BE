package com.influy.domain.answer.repository;

import com.influy.domain.answer.dto.jpql.AnswerJPQLResult;
import com.influy.domain.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
    SELECT 'A' as type, a.id, a.content, a.createdAt, q.id as question_id, q.content as question_content
    FROM Answer a
    JOIN Question q ON a.question.id = q.id
    WHERE q.member.id = :memberId AND a.item.id=:itemId
    """)
    Page<AnswerJPQLResult.UserViewQNAInfo> findAllByMemberIdAndItemId(Long memberId, Long itemId, Pageable pageable);
}
