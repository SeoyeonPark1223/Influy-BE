package com.influy.domain.sellerProfile.repository;

import com.influy.domain.item.dto.ItemResponseDto;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SellerProfileRepository extends JpaRepository<SellerProfile, Long> {
    Optional<SellerProfile> findById(Long id);

    Optional<SellerProfile> findByMemberId(Long memberId);

    boolean existsByEmail(String email);

    boolean existsByInstagram(String instagram);

    @Query("SELECT EXISTS (SELECT 1 FROM QuestionTag t WHERE t.id = :tagId AND t.questionCategory.item.seller.id = :sellerId)")
    Boolean existsByIdAndTagId(@Param("sellerId") Long sellerId,@Param("tagId") Long tagId);

    @Query("SELECT EXISTS (SELECT 1 FROM QuestionCategory c WHERE c.id = :categoryId AND c.item.seller.id = :sellerId)")
    Boolean existsByIdAndCategoryId(@Param("sellerId") Long sellerId, @Param("categoryId") Long categoryId);


    List<SellerProfile> findTop10ByIsPublicTrue();

    @Query("""
        SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
        FROM SellerProfile s
        WHERE s.isPublic IS TRUE
          AND (
            s.member.username LIKE %:keyword%
            OR s.member.nickname LIKE %:keyword%
            OR s.instagram LIKE %:keyword%
          )
    """)
    Boolean existsByIsPublicTrueAndKeywordMatch(@Param("keyword") String keyword);

    @Query("""
        SELECT s FROM SellerProfile s
        WHERE s.isPublic IS TRUE AND (
            s.member.username LIKE %:keyword%
            OR s.member.nickname LIKE %:keyword%
            OR s.instagram LIKE %:keyword%
        )
    """)
    Page<SellerProfile> findByIsPublicTrueAndKeywordMatch(@Param("keyword") String keyword, Pageable pageable);

}
