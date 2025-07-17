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

        Member member = seller.getMember();
        String nickname = null;
        String profileImageUrl =null;
        Long memberId = null;

        if(member != null) {
            nickname = member.getNickname();
            memberId = member.getId();
            profileImageUrl = member.getProfileImg();
        }

        return SellerProfileResponseDTO.SellerProfile.builder()
                .nickname(nickname)
                .profileImg(profileImageUrl)
                .id(memberId)
                .sellerId(seller.getId())
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
        String instagramLink = request.getInstagram().replaceAll("https://www.instagram.com/","");
        int targetIndex = instagramLink.contains("?") ? instagramLink.indexOf("?") : instagramLink.length();

        String instagram = instagramLink.substring(0, targetIndex);



        return SellerProfile.builder()
                .member(member)
                .email(request.getEmail())
                .instagram(instagram)
                .build();
    }
}
