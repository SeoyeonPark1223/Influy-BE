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
    @Query("""
    SELECT q
    FROM Question q
    JOIN q.questionTag qt
    WHERE qt.questionCategory = :questionCategory
    AND (:isAnswered IS NULL OR q.isAnswered = :isAnswered)
    """)
    Page<Question> findAllByQuestionCategory(QuestionCategory category, Pageable pageable);

//    //answer, pending 반환
//    @Query("""
//    SELECT new com.influy.domain.question.dto.JPQLQuestionDTO(qc.id, qc.category, q.isAnswered, COUNT(q))
//    FROM QuestionCategory qc
//    LEFT JOIN Question q ON q.questionCategory = qc
//    WHERE qc.item.id = :itemId
//    GROUP BY qc.id, q.isAnswered
//""")
//    List<JPQLQuestionDTO> countQuestionsGroupedByCategoryAndAnswerStatus(@Param("itemId") Long itemId);


    @Query("""
    SELECT q
    FROM Question q
    JOIN q.questionTag qt
    WHERE qt.questionCategory = :questionCategory
    AND q.isAnswered = :isAnswered
    """)
    Page<Question> findAllByQuestionCategoryAndIsAnswered(QuestionCategory questionCategory, Boolean isAnswered, Pageable pageable);
}
