package com.influy.domain.sellerProfile.repository;

import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SellerProfileRepository extends JpaRepository<SellerProfile, Long> {
    Optional<SellerProfile> findById(Long id);

    Optional<SellerProfile> findByMemberId(Long memberId);

    boolean existsByEmail(String email);

    boolean existsByInstagram(String instagram);
}
