package com.influy.domain.questionTag.repository;

import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionTag.dto.jpql.TagJPQLResult;
import com.influy.domain.questionTag.entity.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionTagRepository extends JpaRepository<QuestionTag, Long> {
    Optional<QuestionTag> findByQuestionCategoryAndName(QuestionCategory questionCategory, String name);
    @Query("""
        SELECT qt FROM QuestionTag qt
        WHERE qt.id = :questionTagId
          AND qt.questionCategory.id = :questionCategoryId
          AND qt.questionCategory.item.id = :itemId
    """)
    Optional<QuestionTag> findValidQuestionTag(Long itemId, Long questionCategoryId, Long questionTagId);

    @Query(value = """
        SELECT qt.id AS id,
               qt.name AS tagName,
               COUNT(q.id) AS totalQuestions,
               MAX(CASE WHEN q.is_checked = false THEN 1 ELSE 0 END) AS uncheckedQuestions
        FROM question_tag qt
        LEFT JOIN question q ON q.question_tag_id = qt.id AND q.is_answered = :isAnswered
        WHERE qt.question_category_id = :categoryId
        GROUP BY qt.id
        ORDER BY uncheckedQuestions,totalQuestions DESC,tagName;
""", nativeQuery = true)
    List<TagJPQLResult.QuestionTagInfo> findTagAndCountByCategoryIdOrderByCount(@Param("categoryId") Long categoryId,
                                                                                @Param("isAnswered") boolean isAnswered);
}
