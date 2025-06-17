package com.influy.domain.question.repository;

import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.entity.Question;
import com.influy.domain.question.dto.JPQLQuestionDTO;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Integer countByQuestionCategoryAndIsAnswered(QuestionCategory category, boolean b);

    Page<Question> findAllByQuestionCategory(QuestionCategory category, Pageable pageable);

    //answer, pending 반환
    @Query("""
    SELECT new com.influy.domain.question.dto.JPQLQuestionDTO(qc.id, qc.category, q.isAnswered, COUNT(q))
    FROM QuestionCategory qc
    LEFT JOIN Question q ON q.questionCategory = qc
    WHERE qc.item.id = :itemId
    GROUP BY qc.id, q.isAnswered
""")
    List<JPQLQuestionDTO> countQuestionsGroupedByCategoryAndAnswerStatus(@Param("itemId") Long itemId);

    //item 에 있는 카테고리별로 대답대기 중인 질문 2개씩 반환
    @Query(value = """
            SELECT *
            FROM
                (SELECT q.id,
                        q.user_id,
                        q.content,
                        q.created_at,
                        q.item_period,
                        qc.id AS category_id,
                        ROW_NUMBER() OVER (PARTITION BY q.question_category_id ORDER BY q.created_at DESC) AS rn
        FROM question q RIGHT OUTER JOIN question_category qc ON q.question_category_id = qc.id
        WHERE qc.item_id = :itemId AND q.is_answered = false) q
            WHERE
                q.rn <= 2
        """,nativeQuery = true)
    List<Object[]> get2QuestionsInCategory(@Param("itemId") Long itemId);

}
