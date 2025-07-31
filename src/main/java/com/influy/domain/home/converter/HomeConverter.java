package com.influy.domain.home.converter;

import com.influy.domain.home.dto.HomeResponseDto;
import com.influy.domain.item.dto.jpql.ItemJPQLResponse;
import com.influy.domain.item.entity.Item;
import com.influy.domain.member.entity.Member;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HomeConverter {
    public static HomeResponseDto.HomeItemViewDto toHomeItemViewDto(Item item, Boolean liked) {
        Member member = item.getSeller().getMember();

        return HomeResponseDto.HomeItemViewDto.builder()
                .sellerNickname(member.getNickname())
                .sellerId(item.getSeller().getId())
                .sellerProfileImg(member.getProfileImg())
                .sellerUsername(member.getUsername())
                .itemId(item.getId())
                .itemName(item.getName())
                .itemMainImg(item.getMainImg())
                .itemPeriod(item.getItemPeriod())
                .startDate(item.getStartDate())
                .endDate(item.getEndDate())
                .tagline(item.getTagline())
                .currentStatus(item.getItemStatus())
                .liked(liked)
                .build();
    }

    public static HomeResponseDto.HomeItemViewPageDto toHomeItemViewPageDto(Page<Item> itemPage, List<Long> likeItems) {
        List<Long> safeLikeItems = (likeItems != null) ? likeItems : Collections.emptyList();

        List<HomeResponseDto.HomeItemViewDto> itemDtoList = itemPage != null
                ? itemPage.stream()
                .map(item -> toHomeItemViewDto(item, safeLikeItems.contains(item.getId())))
                .toList()
                : Collections.emptyList();

        return HomeResponseDto.HomeItemViewPageDto.builder()
                .itemPreviewList(itemDtoList)
                .listSize(itemDtoList.size())
                .totalPage(itemPage == null ? 0: itemPage.getTotalPages())
                .totalElements(itemPage == null ? 0: itemPage.getTotalElements())
                .isFirst(itemPage == null || itemPage.isFirst())
                .isLast(itemPage == null || itemPage.isLast())
                .build();
    }

    public static HomeResponseDto.SellerHomeItemDTO toSellerHomeItemDTO(ItemJPQLResponse.ItemWithQuestionStatus itemJPQLResult, List<String> topNCategories) {
        return HomeResponseDto.SellerHomeItemDTO.builder()
                .itemId(itemJPQLResult.getItem().getId())
                .imageUrl(itemJPQLResult.getItem().getMainImg())
                .itemTitle(itemJPQLResult.getItem().getName())
                .itemStatus(itemJPQLResult.getItem().getItemStatus())
                .itemPeriod(itemJPQLResult.getItem().getItemPeriod())
                .startDate(itemJPQLResult.getItem().getStartDate())
                .endDate(itemJPQLResult.getItem().getEndDate())
                .newQuestions(itemJPQLResult.getNewQuestions())
                .totalPendingQuestions(itemJPQLResult.getPendingQuestions())
                .topCategories(topNCategories)
                .build();
    }

    public static HomeResponseDto.SellerHomeItemPageDTO toSellerHomeItemPageDTO(Page<ItemJPQLResponse.ItemWithQuestionStatus> itmePage, Map<Long, List<String>> topNCategoryMap) {
        List<HomeResponseDto.SellerHomeItemDTO> itemsList = itmePage.stream().map(item->toSellerHomeItemDTO(item, topNCategoryMap.get(item.getItem().getId()))).toList();
        return HomeResponseDto.SellerHomeItemPageDTO.builder()
                .itemList(itemsList)
                .totalPage(itmePage.getTotalPages())
                .totalElements(itmePage.getTotalElements())
                .isFirst(itmePage.isFirst())
                .isLast(itmePage.isLast())
                .listSize(itemsList.size())
                .build();
    }

    public static HomeResponseDto.SellerThumbnailDto toSellerThumbnailDto(SellerProfile seller) {
        Member member = seller.getMember();
        return HomeResponseDto.SellerThumbnailDto.builder()
                .sellerId(seller.getId())
                .profileImg(member.getProfileImg())
                .sellerNickname(member.getNickname())
                .sellerUsername(member.getUsername())
                .build();
    }

    public static HomeResponseDto.SellerThumbnailListDto toSellerThumbnailListDto(List<SellerProfile> sellerList) {
        List<HomeResponseDto.SellerThumbnailDto> sellerDtoList = sellerList.stream().map(HomeConverter::toSellerThumbnailDto).toList();
        return HomeResponseDto.SellerThumbnailListDto.builder()
                .sellerThumbnailList(sellerDtoList)
                .build();
    }

    public static HomeResponseDto.SellerPickItemDto toSellerPickItemDto(Item item) {
        return HomeResponseDto.SellerPickItemDto.builder()
                .itemId(item.getId())
                .mainImg(item.getMainImg())
                .build();
    }

    public static HomeResponseDto.SellerPick3Dto toSellerPick3Dto(SellerProfile seller, List<Item> itemList) {
        return HomeResponseDto.SellerPick3Dto.builder()
                .sellerId(seller.getId())
                .sellerNickname(seller.getMember().getNickname())
                .mainImgList(itemList.stream().map(HomeConverter::toSellerPickItemDto).toList())
                .build();
    }
}
