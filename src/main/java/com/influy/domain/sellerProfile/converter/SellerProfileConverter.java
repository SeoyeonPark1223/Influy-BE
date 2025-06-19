package com.influy.domain.sellerProfile.converter;
import com.influy.domain.sellerProfile.dto.SellerProfileRequestDTO;
import com.influy.domain.sellerProfile.dto.SellerProfileResponseDTO;
import com.influy.domain.sellerProfile.entity.SellerProfile;

public class SellerProfileConverter {

    public static SellerProfile toSeller(SellerProfileRequestDTO.Join requestDTO) {
        //회원가입 로직 구현 시 수정
        return SellerProfile.builder()
                .nickname(requestDTO.getNickname())
                .email(requestDTO.getEmail())
                .build();
    }

    public static SellerProfileResponseDTO.SellerProfile toSellerProfileDTO(SellerProfile seller) {

        return SellerProfileResponseDTO.SellerProfile.builder()
                .id(seller.getId())
                .nickname(seller.getNickname())
                .backgroundImg(seller.getBackgroundImg())
                .profileImg(seller.getProfileImg())
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
}
