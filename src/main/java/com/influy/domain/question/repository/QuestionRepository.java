package com.influy.domain.question.repository;

import com.influy.domain.member.entity.Member;
import com.influy.domain.question.dto.jpql.QuestionJPQLResult;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCategory.entity.QuestionCategory;
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


    @Query("SELECT COUNT(q) AS cnt " +
            "FROM Question q " +
            "JOIN q.item i " +
            "JOIN i.seller s " +
            "WHERE s = :seller AND q.member = :member " +
            "GROUP BY q.member.id")
    Long countByMemberAndSeller(@Param("member") Member member, @Param("seller") SellerProfile seller);

    @Query("SELECT q.member.id AS memberId, COUNT(q) AS cnt " +
            "FROM Question q " +
            "JOIN q.item i " +
            "JOIN i.seller s " +
            "WHERE s = :seller AND q.member.id IN :memberIds " +
            "GROUP BY q.member.id")
    List<QuestionJPQLResult.MemberQuestionCount> countQuestionsBySellerAndMemberIds(@Param("seller") SellerProfile seller,
                                                                                    @Param("memberIds") List<Long> memberIds);



    @Query("""
    SELECT q.id AS id,
           m.id AS memberId,
           m.nickname AS nickname,
           m.username AS username,
           q.content AS content,
           q.createdAt AS createdAt,
           q.isChecked AS isChecked
    FROM Question q
    JOIN q.member m
    WHERE q.questionTag.id = :tagId
      AND q.isAnswered = :isAnswered
""")
    Page<QuestionJPQLResult.SellerViewQuestion> findAllByQuestionTagIdAndIsAnswered(@Param("tagId") Long questionTagId, @Param("isAnswered") Boolean isAnswered, Pageable pageable);

    @Query("""
    SELECT q
    FROM Question q
    JOIN q.questionTag qt
    WHERE qt.questionCategory = :questionCategory
    AND q.isAnswered = :isAnswered
    """)
    Page<Question> findAllByQuestionCategoryAndIsAnswered(QuestionCategory questionCategory, Boolean isAnswered, Pageable pageable);

    @Query("""
        SELECT q FROM Question q
        WHERE q.id = :questionId
          AND q.questionTag.id = :questionTagId
          AND q.questionTag.questionCategory.id = :questionCategoryId
          AND q.questionTag.questionCategory.item.id = :itemId
    """)
    Optional<Question> findValidQuestion(Long itemId, Long questionCategoryId, Long questionTagId, Long questionId);


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
}
