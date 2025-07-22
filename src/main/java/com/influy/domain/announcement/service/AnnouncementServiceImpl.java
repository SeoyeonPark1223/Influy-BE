package com.influy.domain.announcement.service;

import com.influy.domain.announcement.converter.AnnouncementConverter;
import com.influy.domain.announcement.dto.AnnouncementRequestDTO;
import com.influy.domain.announcement.entity.Announcement;
import com.influy.domain.announcement.repository.AnnouncementRepository;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.service.SellerProfileServiceImpl;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.common.PageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final SellerProfileServiceImpl sellerService;

    //공지 리스트 조회
    public Page<Announcement> getAnnouncementsOf(Long sellerId, PageRequestDto page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = page.toPageable(sort);

        SellerProfile seller = sellerService.getSellerProfile(sellerId);
        return announcementRepository.findAllBySeller(seller,pageable);
    }

    //공지 추가
    @Transactional
    public Announcement addAnnouncementOf(SellerProfile seller, AnnouncementRequestDTO requestDTO) {

        Announcement announcement = AnnouncementConverter.toEntity(requestDTO,seller);

        return announcementRepository.save(announcement);

    }

    //특정 공지 수정
    @Transactional
    public Announcement updateAnnouncement(Long announcementId, AnnouncementRequestDTO requestDTO, SellerProfile seller, Boolean isPrimary) {

        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(()->new GeneralException(ErrorStatus.ANNOUNCEMENT_NOT_FOUND));

        //작성자가 다를 경우 업데이트 불가
        if(!announcement.getSeller().equals(seller)){
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }

        return announcement.updateAnnouncement(requestDTO,isPrimary);
    }

    //최상단 공지 조회
    public Optional<Announcement> getPrimaryAnnouncementOf(Long sellerId) {
        SellerProfile seller = sellerService.getSellerProfile(sellerId);

        Optional<Announcement> announcement = Optional.ofNullable(seller.getPrimaryAnnouncement());

        if(announcement.isEmpty()){//최상단 공지가 없으면 가장 최신 공지 제공
            announcement = announcementRepository.findFirstBySellerOrderByCreatedAtDesc(seller);
        }

        return announcement;
    }

    @Transactional
    public void deleteAnnouncement(SellerProfile seller, Long announcementId) {


        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(()->new GeneralException(ErrorStatus.ANNOUNCEMENT_NOT_FOUND));

        //셀러 본인 확인
        if(!announcement.getSeller().equals(seller)){
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }
        //현재 삭제하려는 공지가 최상단 고정 공지라면 해제를 먼저 해준다
        if(seller.getPrimaryAnnouncement()==announcement){
            seller.setPrimaryAnnouncement(null);
        }
        announcementRepository.delete(announcement);
    }

    @Override
    public Integer getTotalAnnouncementsOf(Long sellerId) {
        SellerProfile seller = sellerService.getSellerProfile(sellerId);
        return announcementRepository.countBySeller(seller);
    }
}
