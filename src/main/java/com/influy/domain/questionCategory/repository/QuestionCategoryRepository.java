package com.influy.domain.questionCategory.repository;

import com.influy.domain.item.entity.Item;
import com.influy.domain.questionCategory.dto.jpql.CategoryJPQLResult;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {
    boolean existsByItemIdAndName(Long itemId, String name);
    boolean existsByName(String name);

    //카테고리 아이디, 이름, isAnswered 에 따른 전체 질문 수, 미확인 질문 수
    @Query(value = """
        SELECT qc.id AS id,
               qc.name AS categoryName,
               COUNT(q.id) AS totalQuestions,
               SUM(CASE WHEN q.is_checked = false THEN 1 ELSE 0 END) AS uncheckedQuestions
        FROM question_category qc
        LEFT JOIN question_tag qt ON qt.question_category_id = qc.id
        LEFT JOIN question q ON q.question_tag_id = qt.id AND q.is_answered = :isAnswered
        WHERE qc.item_id = :itemId
        GROUP BY qc.id
        ORDER BY uncheckedQuestions DESC,totalQuestions DESC, categoryName ASC;
""", nativeQuery = true)
    List<CategoryJPQLResult.CategoryInfo> findQuestionCategories(@Param("itemId") Long itemId, @Param("isAnswered") Boolean isAnswered);


    Optional<QuestionCategory> findByIdAndItemId(Long questionCategoryId, Long itemId);

    List<QuestionCategory> findAllByItemId(Long itemId);

    @Query(value = """
    SELECT item_id AS itemId, category_name AS categoryName
    FROM (
        SELECT
            q.item_id AS item_id,
            c.name AS category_name,
            RANK() OVER (PARTITION BY q.item_id ORDER BY COUNT(*) DESC) AS rk
        FROM question q
        JOIN question_tag qt ON q.question_tag_id = qt.id
        JOIN question_category c ON qt.question_category_id = c.id
        WHERE q.item_id IN (:itemIds)
          AND q.is_checked = false
        GROUP BY q.item_id, c.id
    ) ranked
    WHERE rk <= 2
    """, nativeQuery = true)
    List<CategoryJPQLResult.Top2Categories> getTop2CategoriesByNewQuestion(@Param("itemIds") List<Long> itemIds);
}
