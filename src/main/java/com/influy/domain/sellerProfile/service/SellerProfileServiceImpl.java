package com.influy.domain.sellerProfile.service;

import com.influy.domain.home.converter.HomeConverter;
import com.influy.domain.home.dto.HomeResponseDto;
import com.influy.domain.image.service.ImageService;
import com.influy.domain.item.dto.jpql.ItemJPQLResponse.IsArchivedItemCount;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.like.entity.LikeStatus;
import com.influy.domain.like.repository.LikeRepository;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.sellerProfile.converter.SellerProfileConverter;
import com.influy.domain.sellerProfile.dto.SellerProfileRequestDTO;
import com.influy.domain.sellerProfile.entity.ItemSortType;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SellerProfileServiceImpl implements SellerProfileService {

    private final ItemRepository itemRepository;
    private final SellerProfileRepository sellerProfileRepository;
    private final LikeRepository likeRepository;
    private final ImageService imageService;

    public SellerProfile getSellerProfile(Long sellerId){
        return sellerProfileRepository.findById(sellerId).orElseThrow(()->new GeneralException(ErrorStatus.SELLER_NOT_FOUND));
    }

    @Override
    @Transactional
    public SellerProfile updateSeller(SellerProfile sellerProfile, SellerProfileRequestDTO.UpdateProfile request) {

        List<String> images = new ArrayList<>();

        Member member = sellerProfile.getMember();
        //프사 변경 검사
        if(!Objects.equals(request.getProfile().getProfileUrl(),member.getProfileImg())){
            String img = member.getProfileImg();
            if(img!=null) images.add(img);
            member.updateProfile(request.getProfile());
        }


        //배경사진 변경 검사
        if(!Objects.equals(request.getBackgroundImg(),sellerProfile.getBackgroundImg())){
            String bgImg = sellerProfile.getBackgroundImg();
            if(bgImg!=null) images.add(bgImg);
        }

        if(!images.isEmpty()){
            imageService.deleteImg(images);
        }

        return sellerProfile.setProfile(request);
    }

    @Override
    @Transactional
    public SellerProfile updateItemSortType(SellerProfile sellerProfile, ItemSortType sortBy) {

        return sellerProfile.setItemSortType(sortBy);
    }

    @Override
    @Transactional
    public SellerProfile updateIsPublic(SellerProfile sellerProfile, Boolean isPublic) {

        return sellerProfile.setIsPublic(isPublic);
    }



    @Override
    @Transactional
    public SellerProfile createSellerProfile(Member member, MemberRequestDTO.SellerJoin request) {

        SellerProfile sellerProfile = SellerProfileConverter.toSellerProfile(member,request);
        member.setSellerProfile(sellerProfile);
        return sellerProfileRepository.save(sellerProfile);
    }

    @Override
    public boolean getIsLikedByMember(SellerProfile seller, Member member) {

        return likeRepository.existsByMemberAndSellerAndLikeStatus(member,seller,LikeStatus.LIKE);
    }

    @Override
    public List<IsArchivedItemCount> getMarketItems(Long sellerId) {
        return itemRepository.countBySellerIdGroupByIsArchived(sellerId);
    }

    @Override
    public Boolean checkQuestionOwner(Long tagId, Long categoryId, Long sellerId) {
        if(tagId!=null){
            if(!sellerProfileRepository.existsByIdAndTagId(sellerId, tagId)){
                throw new GeneralException(ErrorStatus.NOT_OWNER);
            }
        }else if(categoryId!=null){
            if(!sellerProfileRepository.existsByIdAndCategoryId(sellerId, categoryId)){
                throw new GeneralException(ErrorStatus.NOT_OWNER);
            }
        }else return false;

        return true;
    }

    @Override
    public HomeResponseDto.SellerThumbnailDto getOverview(Long sellerId) {
        SellerProfile seller = sellerProfileRepository.findById(sellerId)
                .orElseThrow(()->new GeneralException(ErrorStatus.SELLER_NOT_FOUND));
        return HomeConverter.toSellerThumbnailDto(seller);
    }
}
