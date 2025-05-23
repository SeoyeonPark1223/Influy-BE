package com.influy.domain.announcement.repository;

import com.influy.domain.announcement.entity.Announcement;
import com.influy.domain.seller.entity.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    Page<Announcement> findAllBySeller(Seller seller, Pageable pageable);

    Optional<Announcement> findFirstBySellerOrderByCreatedAtDesc(Seller seller);
}
