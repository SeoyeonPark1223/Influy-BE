package com.influy.domain.seller.converter;
import com.influy.domain.seller.dto.SellerRequestDTO;
import com.influy.domain.seller.dto.SellerResponseDTO;
import com.influy.domain.seller.entity.ItemSortType;
import com.influy.domain.seller.entity.Seller;

import java.util.List;

public class SellerConverter {

    public static Seller toSeller(SellerRequestDTO.Join requestDTO) {
        //회원가입 로직 구현 시 수정
        return Seller.builder()
                .nickname(requestDTO.getNickname())
                .email(requestDTO.getEmail())
                .build();
    }

    public static SellerResponseDTO.SellerProfile toSellerProfileDTO(Seller seller) {

        return SellerResponseDTO.SellerProfile.builder()
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

    public static SellerResponseDTO.SortType toSortTypeDTO(Seller seller) {
        return SellerResponseDTO.SortType.builder().itemSortType(seller.getItemSortType()).build();
    }
}
