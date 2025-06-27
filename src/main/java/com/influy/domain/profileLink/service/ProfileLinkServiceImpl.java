package com.influy.domain.profileLink.service;

import com.influy.domain.profileLink.converter.ProfileLinkConverter;
import com.influy.domain.profileLink.dto.ProfileLinkRequestDTO;
import com.influy.domain.profileLink.entity.ProfileLink;
import com.influy.domain.profileLink.repository.ProfileLinkRepository;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.service.SellerProfileServiceImpl;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileLinkServiceImpl implements ProfileLinkService {

    private final ProfileLinkRepository profileLinkRepository;
    private final SellerProfileServiceImpl sellerService;

    //링크 생성
    @Override
    @Transactional
    public ProfileLink createLinkOf(Long sellerId, ProfileLinkRequestDTO request) {
        SellerProfile seller = sellerService.getSeller(sellerId);
        Integer linkCount = profileLinkRepository.countBySeller(seller);

        if(linkCount==5){
            throw new GeneralException(ErrorStatus.LINK_COUNT_LIMIT);
        }
        ProfileLink profileLink = ProfileLinkConverter.toEntity(request,seller);
        return profileLinkRepository.save(profileLink);
    }

    //링크 수정
    @Override
    @Transactional
    public ProfileLink updateLinkOf(Long sellerId, Long linkId, ProfileLinkRequestDTO request) {
        SellerProfile seller = sellerService.getSeller(sellerId);
        ProfileLink profileLink = profileLinkRepository.findById(linkId).orElseThrow(()->new GeneralException(ErrorStatus.LINK_NOT_FOUND));

        if(request.getLinkName()!=null) {
            profileLink.setLinkName(request.getLinkName());
        }
        if(request.getLink()!=null) {
            profileLink.setLink(request.getLink());
        }
        return profileLink;
    }

    //링크 리스트 조회
    @Override
    public List<ProfileLink> getLinkListOf(Long sellerId) {
        SellerProfile seller = sellerService.getSeller(sellerId);

        return profileLinkRepository.findAllBySellerOrderByCreatedAt(seller);
    }

    @Override
    @Transactional
    public void deleteLinkOf(Long sellerId, Long linkId) {
        SellerProfile seller = sellerService.getSeller(sellerId);
        ProfileLink profileLink = profileLinkRepository.findById(linkId).orElseThrow(()->new GeneralException(ErrorStatus.LINK_NOT_FOUND));

        //본인 검증
        if(profileLink.getSeller()!=seller) {
            throw new GeneralException(ErrorStatus.FORBIDDEN);
        }
        profileLinkRepository.delete(profileLink);
    }
}
