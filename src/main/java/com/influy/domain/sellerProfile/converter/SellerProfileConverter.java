package com.influy.domain.sellerProfile.converter;
import com.influy.domain.item.dto.jpql.ItemJPQLResponse;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.sellerProfile.dto.SellerProfileRequestDTO;
import com.influy.domain.sellerProfile.dto.SellerProfileResponseDTO;
import com.influy.domain.sellerProfile.entity.SellerProfile;

import java.util.List;

public class SellerProfileConverter {

    public static SellerProfile toSeller(SellerProfileRequestDTO.Join requestDTO) {
        return null;
    }

    public static SellerProfileResponseDTO.SellerProfile toSellerProfileDTO(SellerProfile seller) {

        Member member = seller.getMember();


        return SellerProfileResponseDTO.SellerProfile.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .username(member.getUsername())
                .profileImg(member.getProfileImg())
                .sellerId(seller.getId())
                .backgroundImg(seller.getBackgroundImg())
                .instagram(seller.getInstagram())
                .tiktok(seller.getTiktok())
                .youtube(seller.getYoutube())
                .email(seller.getEmail())
                .build();
    }

    public static SellerProfileResponseDTO.SortType toSortTypeDTO(SellerProfile seller) {
        return SellerProfileResponseDTO.SortType.builder().itemSortType(seller.getItemSortType()).build();
    }

    public static SellerProfile toSellerProfile(Member member, MemberRequestDTO.SellerJoin request) {
        String instagramLink = request.getInstagram().replaceAll("https://www.instagram.com/","").replaceAll("https://instagram.com/","");
        int targetIndex = instagramLink.contains("?") ? instagramLink.indexOf("?") : instagramLink.length();

        String instagram = instagramLink.substring(0, targetIndex);



        return SellerProfile.builder()
                .member(member)
                .email(request.getEmail())
                .instagram(instagram)
                .tiktok(request.getTiktok())
                .youtube(request.getYoutube())
                .build();
    }

    public static SellerProfileResponseDTO.MarketProfile toMarketProfileDTO(SellerProfile seller, boolean isLiked, List<ItemJPQLResponse.ItemCount> itemCountList) {
        SellerProfileResponseDTO.SellerProfile sellerProfileDTO= toSellerProfileDTO(seller);
        Long publicItems = null;
        Long privateItems = null;
        for(ItemJPQLResponse.ItemCount itemCount : itemCountList) {
            if(itemCount.getIsArchived()){
                privateItems = itemCount.getCount();
            }else {
                publicItems = itemCount.getCount();
            }
        }
        return SellerProfileResponseDTO.MarketProfile.builder()
                .sellerProfile(sellerProfileDTO)
                .isLiked(isLiked)
                .isPublic(seller.getIsPublic())
                .itemSortType(seller.getItemSortType())
                .privateItemCnt(privateItems)
                .publicItemCnt(publicItems)
                .reviews(0L)
                .build();

    }


}
