package com.influy.domain.announcement.service;

import com.influy.domain.announcement.converter.AnnouncementConverter;
import com.influy.domain.announcement.dto.AnnouncementRequestDTO;
import com.influy.domain.announcement.entity.Announcement;
import com.influy.domain.announcement.repository.AnnouncementRepository;
import com.influy.domain.seller.entity.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public Page<Announcement> getAnnouncementsOf(Seller seller, Pageable pageable) {
        //seller 관련 pr 머지되면 셀러 리포지토리에서 existBy로 검증
        return announcementRepository.findAllBySeller(seller,pageable);
    }

    //공지 추가
    @Transactional
    public Announcement addAnnouncementOf(Long sellerId, AnnouncementRequestDTO requestDTO) {
        Seller seller = new Seller(sellerId,"나",true,null,null,null,null,null,
                "string",null,null,null,null,null,null,null);

        Announcement announcement = AnnouncementConverter.toEntity(requestDTO);
        return announcementRepository.save(announcement);

    }
}
