package com.influy.domain.profileLink.repository;

import com.influy.domain.profileLink.entity.ProfileLink;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileLinkRepository extends JpaRepository<ProfileLink, Long> {
    List<ProfileLink> findBySeller(SellerProfile seller);

    Integer countBySeller(SellerProfile seller);

    List<ProfileLink> findAllBySellerIdOrderByCreatedAt(Long sellerId);
}
