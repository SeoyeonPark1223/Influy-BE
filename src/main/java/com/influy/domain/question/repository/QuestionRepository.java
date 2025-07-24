package com.influy.domain.question.repository;

import com.influy.domain.answer.dto.jpql.AnswerJPQLResult;
import com.influy.domain.question.dto.jpql.QuestionJPQLResult;
import com.influy.domain.question.entity.Question;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {




    @Query("SELECT q.member.id AS memberId, COUNT(q) AS cnt " +
            "FROM Question q " +
            "JOIN q.item i " +
            "JOIN i.seller s " +
            "WHERE s = :seller AND q.member.id IN :memberIds " +
            "GROUP BY q.member.id")
    List<QuestionJPQLResult.MemberQuestionCount> countQuestionsBySellerAndMemberIds(@Param("seller") SellerProfile seller,
                                                                                    @Param("memberIds") List<Long> memberIds);


    //태그별 질문 조회
    @Query("""
    SELECT q.id AS id,
           m.id AS memberId,
           m.nickname AS nickname,
           m.username AS username,
           q.content AS content,
           q.createdAt AS createdAt,
           q.isChecked AS isChecked,
           qt.name AS tagName
    FROM Question q
    JOIN q.member m
    JOIN q.questionTag qt
    WHERE q.questionTag.id = :tagId AND q.isHidden = false
      AND q.isAnswered = :isAnswered
""")
    Page<QuestionJPQLResult.SellerViewQuestion> findAllByQuestionTagIdAndIsAnswered(@Param("tagId") Long questionTagId, @Param("isAnswered") Boolean isAnswered, Pageable pageable);

    //카테고리에 속한 모든 질문 조회
    @Query("""
    SELECT q.id AS id,
           m.id AS memberId,
           m.nickname AS nickname,
           m.username AS username,
           q.content AS content,
           q.createdAt AS createdAt,
           q.isChecked AS isChecked,
           qt.name AS tagName
    FROM Question q
    JOIN q.member m
    JOIN q.questionTag qt
    WHERE qt.questionCategory.id = :categoryId
      AND q.isAnswered = :isAnswered
      AND q.isHidden = false
    ORDER BY q.createdAt
    
    """)
    Page<QuestionJPQLResult.SellerViewQuestion> findAllByQuestionCategoryAndIsAnswered(@Param("categoryId") Long questionCategoryId, @Param("isAnswered") Boolean isAnswered, Pageable pageable);

    @Query("""
        SELECT q FROM Question q
        WHERE q.id = :questionId
          AND q.questionTag.id = :questionTagId
          AND q.questionTag.questionCategory.id = :questionCategoryId
          AND q.questionTag.questionCategory.item.id = :itemId
    """)
    Optional<Question> findValidQuestion(@Param("itemId") Long itemId, @Param("questionCategoryId") Long questionCategoryId, @Param("questionTagId") Long questionTagId, @Param("questionId") Long questionId);


    /*
     밑 3개는 태그, 카테고리, 아이템 별 새 질문 수 구하는 함수
     */
    @Query("""
    SELECT COUNT(q)
    FROM QuestionTag qt
    JOIN Question q ON q.questionTag = qt
    WHERE qt.questionCategory.id = :categoryId
      AND q.isAnswered = true
      AND q.isChecked = false
    """)
    Long countByQuestionCategoryIdAndIsCheckedFalse(@Param("categoryId") Long categoryId);

    Long countByQuestionTagIdAndIsCheckedFalse(Long questionTagId);

    Long countByItemIdAndIsCheckedFalse(Long itemId);

    @Modifying
    @Query("UPDATE Question q SET q.isChecked = true WHERE q.id IN :ids AND q.isChecked = false")
    void setQuestionsAsChecked(@Param("ids") List<Long> questionIds);

    @Query(value = """

    SELECT 'Q' AS type, q.id AS id, q.content AS content, q.created_at AS createdAt, NULL AS questionId, NULL as questionContent
    FROM question q
    WHERE q.member_id = :memberId
    
    UNION ALL
    
    SELECT 'A' AS type, a.id AS id, a.content AS content, a.created_at AS createdAt, q.id AS questionId, q.content AS questionContent
    FROM answer a
    JOIN question q ON a.question_id = q.id
    WHERE q.member_id = :memberId AND q.item_id = :itemId
    
    ORDER BY createdAt DESC
    """, nativeQuery = true)
    Page<AnswerJPQLResult.UserViewQNAInfo> findAllByMemberIdAndItemId(@Param("memberId") Long memberId, @Param("itemId") Long itemId, Pageable pageable);
}
