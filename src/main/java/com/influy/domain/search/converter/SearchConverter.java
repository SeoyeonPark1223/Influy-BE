package com.influy.domain.search.converter;

import com.influy.domain.item.converter.ItemConverter;
import com.influy.domain.item.entity.Item;
import com.influy.domain.member.entity.Member;
import com.influy.domain.search.dto.SearchResponseDto;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

public class SearchConverter {
    public static SearchResponseDto.SearchResultDto toSearchResultDto(Page<SellerProfile> sellerPage, Page<Item> itemPage, List<Long> likeSellers, List<Long> likeItems) {
        return SearchResponseDto.SearchResultDto.builder()
                .sellerPageDtoList(sellerPage == null ? null : toSellerPageResultDto(sellerPage, likeSellers))
                .itemPageDtoList(itemPage == null ? null : ItemConverter.toHomeItemViewPageDto(itemPage, likeItems))
                .build();

    }

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
                .totalPage(sellerPage.getTotalPages())
                .totalElements(sellerPage.getTotalElements())
                .isFirst(sellerPage.isFirst())
                .isLast(sellerPage.isLast())
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
