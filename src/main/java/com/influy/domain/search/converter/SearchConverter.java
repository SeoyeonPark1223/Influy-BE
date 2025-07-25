package com.influy.domain.search.converter;

import com.influy.domain.member.entity.Member;
import com.influy.domain.search.dto.SearchResponseDto;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

public class SearchConverter {
    public static SearchResponseDto.SellerPageResultDto toSellerPageResultDto(Page<SellerProfile> sellerPage, List<Long> likeSellers) {
        List<Long> safeLikeSellers = (likeSellers != null) ? likeSellers : Collections.emptyList();

        List<SearchResponseDto.SellerResultDto> sellerDtoList = sellerPage != null
                ? sellerPage.stream()
                .map(seller -> toSellerResultDto(seller, safeLikeSellers.contains(seller.getId())))
                .toList()
                : Collections.emptyList();

        return SearchResponseDto.SellerPageResultDto.builder()
                .sellerDtoList(sellerDtoList)
                .listSize(sellerDtoList.size())
                .totalPage(sellerPage == null? 0 : sellerPage.getTotalPages())
                .totalElements(sellerPage == null? 0 : sellerPage.getTotalElements())
                .isFirst(sellerPage == null || sellerPage.isFirst())
                .isLast(sellerPage == null || sellerPage.isLast())
                .build();
    }

    public static SearchResponseDto.SellerResultDto toSellerResultDto(SellerProfile seller, Boolean liked) {
        Member member = seller.getMember();

        return SearchResponseDto.SellerResultDto.builder()
                .sellerId(seller.getId())
                .sellerProfileImg(member.getProfileImg())
                .sellerUsername(member.getUsername())
                .sellerNickname(member.getNickname())
                .liked(liked)
                .build();
    }
}
