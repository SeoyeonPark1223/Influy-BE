package com.influy.domain.question.repository;

import com.influy.domain.member.entity.Member;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.dto.jpql.JPQLResult;
import com.influy.domain.answer.entity.Answer;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {


    Long countByMemberAndSeller(Member member, SellerProfile seller);

    @Query("SELECT q.member.id AS memberId, COUNT(q) AS cnt " +
            "FROM Question q " +
            "WHERE q.seller = :seller AND q.member.id IN :memberIds " +
            "GROUP BY q.member.id")
    List<JPQLResult.MemberQuestionCount> countQuestionsBySellerAndMemberIds(@Param("seller") SellerProfile seller,
                                                                            @Param("memberIds") List<Long> memberIds);


    Page<Question> findAllByQuestionTagIdAndIsAnswered(Long questionTagId, Boolean isAnswered, Pageable pageable);
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
}
