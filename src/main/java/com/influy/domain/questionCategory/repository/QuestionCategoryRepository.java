package com.influy.domain.questionCategory.repository;

import com.influy.domain.item.entity.Item;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {
    boolean existsByItemIdAndName(Long itemId, String name);
    boolean existsByName(String name);
    @Query("""
    SELECT qc
    FROM QuestionCategory qc
    LEFT JOIN QuestionTag qt ON qt.questionCategory = qc
    LEFT JOIN Question q ON q.questionTag = qt
    WHERE qc.item.id = :itemId AND qc.item.seller.id = :sellerId
    GROUP BY qc
    ORDER BY COUNT(q.id) DESC
    """)
    List<QuestionCategory> findQuestionCategories(@Param("itemId") Long itemId, @Param("sellerId") Long sellerId);

    @Query("""
    SELECT COUNT(q)
    FROM QuestionTag qt
    LEFT JOIN Question q ON q.questionTag = qt
    WHERE qt.questionCategory.id = :questionCategoryId
    """)
    int countQuestionsByCategoryId(@Param("questionCategoryId") Long questionCategoryId);

    List<QuestionCategory> findAllByItem(Item item);
}
