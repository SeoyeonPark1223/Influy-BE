package com.influy.domain.announcement.service;

import com.influy.domain.announcement.dto.AnnouncementRequestDTO;
import com.influy.domain.announcement.entity.Announcement;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.common.PageRequestDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AnnouncementService {
    Page<Announcement> getAnnouncementsOf(Long sellerId, PageRequestDto pageable);

    Optional<Announcement> getPrimaryAnnouncementOf(Long sellerId);

    Announcement addAnnouncementOf(SellerProfile seller, AnnouncementRequestDTO requestDTO);

    Announcement updateAnnouncement(Long announcementId, AnnouncementRequestDTO requestDTO, SellerProfile seller, Boolean isPrimary);

    void deleteAnnouncement(SellerProfile seller, Long announcementId);

    Integer getTotalAnnouncementsOf(Long sellerId);
}
