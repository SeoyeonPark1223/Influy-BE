package com.influy.domain.announcement.repository;

import com.influy.domain.announcement.entity.Announcement;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    Page<Announcement> findAllBySeller(SellerProfile seller, Pageable pageable);

    Optional<Announcement> findFirstBySellerOrderByCreatedAtDesc(SellerProfile seller);

    Integer countBySeller(SellerProfile seller);
}
