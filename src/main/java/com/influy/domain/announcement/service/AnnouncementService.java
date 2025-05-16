package com.influy.domain.announcement.service;

import com.influy.domain.announcement.converter.AnnouncementConverter;
import com.influy.domain.announcement.dto.AnnouncementRequestDTO;
import com.influy.domain.announcement.entity.Announcement;
import com.influy.domain.announcement.repository.AnnouncementRepository;
import com.influy.domain.seller.entity.Seller;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public Page<Announcement> getAnnouncementsOf(Long sellerId, Pageable pageable) {
        //seller 관련 pr 머지되면 셀러 리포지토리에서 existBy로 검증
        Seller seller = new Seller(sellerId,"나",true,null,null,null,null,null,
                "string",null,null,null,null,null,null,null);

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

    //특정 공지 수정
    public Announcement updateAnnouncement(Long announcementId, AnnouncementRequestDTO requestDTO, Long sellerId) {

        //repo에서 seller 찾아오기
        Seller seller = new Seller(2L,"나",true,null,null,null,null,null,
                "string",null,null,null,null,null,null,null);

        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(()->new GeneralException(ErrorStatus.ANNOUNCEMENT_NOT_FOUND));

        //작성자가 다를 경우 업데이트 불가
        if(!announcement.getSeller().equals(seller)){
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }

        return announcement.updateAnnouncement(requestDTO);
    }
}
