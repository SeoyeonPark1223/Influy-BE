package com.influy.domain.seller.converter;

import com.influy.domain.profileLink.entity.ProfileLink;
import com.influy.domain.profileLink.entity.ProfileLinkType;
import com.influy.domain.seller.dto.SellerRequestDTO;
import com.influy.domain.seller.dto.SellerResponseDTO;
import com.influy.domain.seller.entity.Seller;

import java.util.List;

public class SellerConverter {

    public static Seller toSeller(SellerRequestDTO sellerRequestDTO) {
        return Seller.builder().build();
    }

    public static SellerResponseDTO.SellerProfile toSellerProfileDTO(Seller seller, List<ProfileLink> profileLinks) {

        //findBySellerAndLinkTypeNot(id,market)

        //빌더 객체 생성
        SellerResponseDTO.SellerProfile.SellerProfileBuilder sellerProfileBuilder = SellerResponseDTO.SellerProfile.builder();

        for(ProfileLink profileLink : profileLinks) {

            ProfileLinkType type = profileLink.getLinkType();

            //각 링크 타입에 따라 builder 객체에 값 대입
            if(type.equals(ProfileLinkType.INSTAGRAM)){
                sellerProfileBuilder.instagram(profileLink.getLink());
            }
            else if(type.equals(ProfileLinkType.TIKTOK)){
                sellerProfileBuilder.tiktok(profileLink.getLink());
            }
            else if (type.equals(ProfileLinkType.YOUTUBE)) {
                sellerProfileBuilder.youtube(profileLink.getLink());
            }
            else if(type.equals(ProfileLinkType.EMAIL)){
                sellerProfileBuilder.email(profileLink.getLink());
            }
        }

        return sellerProfileBuilder
                .id(seller.getId())
                .nickname(seller.getNickname())
                .backgroundImg(seller.getBackgroundImg())
                .profileImg(seller.getProfileImg())
                .isPublic(seller.getIsPublic())
                .itemSortType(seller.getItemSortType())
                .build();
    }
}
