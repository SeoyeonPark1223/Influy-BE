package com.influy.domain.sellerProfile.repository;

import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerProfileRepository extends JpaRepository<SellerProfile, Long> {
    Optional<SellerProfile> findById(Long id);

    Optional<SellerProfile> findByMemberId(Long memberId);

    boolean existsByEmail(String email);

    boolean existsByInstagram(String instagram);

    Boolean existsByMemberUsernameContainingOrMemberNicknameContainingOrInstagramContaining(String username, String nickname, String instagram);

    Page<SellerProfile> findAllByMemberUsernameContainingOrMemberNicknameContainingOrInstagramContaining(String username, String nickname, String instagram, Pageable pageable);
}
