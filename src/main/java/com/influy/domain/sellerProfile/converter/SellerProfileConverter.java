package com.influy.domain.sellerProfile.converter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.sellerProfile.dto.SellerProfileRequestDTO;
import com.influy.domain.sellerProfile.dto.SellerProfileResponseDTO;
import com.influy.domain.sellerProfile.entity.SellerProfile;

public class SellerProfileConverter {

    public static SellerProfile toSeller(SellerProfileRequestDTO.Join requestDTO) {
        return null;
    }

    public static SellerProfileResponseDTO.SellerProfile toSellerProfileDTO(SellerProfile seller) {

        return SellerProfileResponseDTO.SellerProfile.builder()
                .id(seller.getId())
                .backgroundImg(seller.getBackgroundImg())
                .isPublic(seller.getIsPublic())
                .itemSortType(seller.getItemSortType())
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
        return SellerProfile.builder()
                .member(member)
                .email(request.getEmail())
                .instagram(request.getInstagram())
                .build();
    }
}
