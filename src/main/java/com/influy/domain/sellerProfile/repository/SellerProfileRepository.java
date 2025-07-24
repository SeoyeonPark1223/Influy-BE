package com.influy.domain.sellerProfile.repository;

import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
