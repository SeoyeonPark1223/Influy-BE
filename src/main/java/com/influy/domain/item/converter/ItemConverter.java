package com.influy.domain.item.converter;

import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.domain.faqCard.dto.FaqCardResponseDto;
import com.influy.domain.item.dto.ItemRequestDto;
import com.influy.domain.item.dto.ItemResponseDto;
import com.influy.domain.item.entity.Item;
import com.influy.domain.image.entity.Image;
import com.influy.domain.item.entity.ItemStatus;
import com.influy.domain.item.entity.TalkBoxInfoPair;
import com.influy.domain.item.entity.TalkBoxOpenStatus;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ItemConverter {
    public static Item toItem(SellerProfile seller, ItemRequestDto.DetailDto request) {
        return Item.builder()
                .seller(seller)
                .name(request.getName())
                .regularPrice(request.getRegularPrice())
                .salePrice(request.getSalePrice())
                .tagline(request.getTagline())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .itemPeriod(request.getItemPeriod())
                .marketLink(request.getMarketLink())
                .comment(request.getComment())
                .isArchived(request.getIsArchived())
                .build();
    }

    public static ItemResponseDto.ResultDto toResultDto(Item item) {
        return ItemResponseDto.ResultDto.builder()
                .itemId(item.getId())
                .build();
    }

    public static ItemResponseDto.ResultDto toResultDto(Long itemId) {
        return ItemResponseDto.ResultDto.builder()
                .itemId(itemId)
                .build();
    }

    public static ItemResponseDto.DetailPreviewDto toDetailPreviewDto(Item item, boolean liked, MemberRole memberRole, Integer waitingCnt, Integer completedCnt) {
        return ItemResponseDto.DetailPreviewDto.builder()
                .itemId(item.getId())
                .sellerId(item.getSeller().getId())
                .MainImg(item.getImageList().getFirst().getImageLink())
                .itemPeriod(item.getItemPeriod())
                .itemName(item.getName())
                .sellerName(item.getSeller().getMember().getUsername())
                .startDate(item.getStartDate())
                .endDate(item.getEndDate())
                .tagline(item.getTagline())
                .currentStatus(item.getItemStatus())
                .liked(liked)
                .talkBoxInfo(memberRole==MemberRole.SELLER ? toTalkBoxInfoDto(item, waitingCnt, completedCnt):null)
                .build();
    }

    public static ItemResponseDto.DetailPreviewPageDto toDetailPreviewPageDto(Page<Item> itemPage, List<Long> likeItems, MemberRole memberRole, Map<Long, Integer> waitingCntMap, Map<Long, Integer> completedCntMap) {
        List<Long> safeLikeItems = (likeItems != null) ? likeItems : Collections.emptyList();

        List<ItemResponseDto.DetailPreviewDto> itemPreviewList = itemPage != null
                ? itemPage.stream()
                .map(item -> {
                    Long itemId = item.getId();
                    boolean liked = safeLikeItems.contains(item.getId());
                    Integer waitingCnt = waitingCntMap.getOrDefault(itemId, 0);
                    Integer completedCnt = completedCntMap.getOrDefault(itemId, 0);
                    return toDetailPreviewDto(item, liked, memberRole, waitingCnt, completedCnt);
                })
                .toList()
                : Collections.emptyList();

        return ItemResponseDto.DetailPreviewPageDto.builder()
                .itemPreviewList(itemPreviewList)
                .listSize(itemPreviewList.size())
                .totalPage(itemPage != null? itemPage.getTotalPages() : 0)
                .totalElements(itemPage != null? itemPage.getTotalElements() : 0)
                .isFirst(itemPage == null || itemPage.isFirst())
                .isLast(itemPage == null || itemPage.isLast())
                .build();
    }

    public static ItemResponseDto.TalkBoxInfoDto toTalkBoxInfoDto(Item item, Integer waitingCnt, Integer completedCnt) {
        return ItemResponseDto.TalkBoxInfoDto.builder()
                .talkBoxOpenStatus(item.getTalkBoxOpenStatus())
                .waitingCnt(waitingCnt)
                .completedCnt(completedCnt)
                .build();
    }

    public static ItemResponseDto.DetailViewDto toDetailViewDto(Item item) {
        List<String> itemImgLinkList = item.getImageList().stream()
                .map(Image::getImageLink).toList();

        List<String> itemCategoryList = item.getItemCategoryList().stream()
                .map(ic -> ic.getCategory().getCategory())
                .toList();

        return ItemResponseDto.DetailViewDto.builder()
                .itemId(item.getId())
                .itemPeriod(item.getItemPeriod())
                .itemName(item.getName())
                .startDate(item.getStartDate())
                .endDate(item.getEndDate())
                .tagline(item.getTagline())
                .currentStatus(item.getItemStatus())
                .marketLink(item.getMarketLink())
                .isArchived(item.getIsArchived())
                .regularPrice(item.getRegularPrice())
                .salePrice(item.getSalePrice())
                .itemImgList(itemImgLinkList)
                .itemCategoryList(itemCategoryList)
                .build();
    }

    public static ItemResponseDto.CountDto toCountDto(Long sellerId, Integer count) {
       return ItemResponseDto.CountDto.builder()
               .sellerId(sellerId)
               .count(count)
               .build();
    }

    public static ItemResponseDto.ItemOverviewDto toItemOverviewDto(Item item) {
        return ItemResponseDto.ItemOverviewDto.builder()
                .id(item.getId())
                .itemName(item.getName())
                .tagline(item.getTagline())
                .mainImg(item.getImageList().getFirst().getImageLink())
                .talkBoxOpenStatus(item.getTalkBoxOpenStatus())
                .build();
    }

    public static ItemResponseDto.TalkBoxOpenStatusDto toTalkBoxOpenStatusDto(Long itemId, TalkBoxOpenStatus openStatus) {
        return ItemResponseDto.TalkBoxOpenStatusDto.builder()
                .itemId(itemId)
                .status(openStatus)
                .build();
    }

    public static ItemResponseDto.ViewTalkBoxCommentDto toViewTalkBoxCommentDto(SellerProfile seller, Item item) {
        Member member = seller.getMember();
        return ItemResponseDto.ViewTalkBoxCommentDto.builder()
                .sellerId(seller.getId())
                .sellerProfileImg(member.getProfileImg())
                .sellerUsername(member.getUsername())
                .sellerNickname(member.getNickname())
                .createdAt(LocalDateTime.now())
                .talkBoxComment(item.getTalkBoxComment())
                .build();
    }

    public static ItemResponseDto.TalkBoxOpenedDto toTalkBoxOpenedDto(Item item, Integer waitingCnt, Integer completedCnt, Integer unCheckedCnt) {
        return ItemResponseDto.TalkBoxOpenedDto.builder()
                .itemId(item.getId())
                .itemMainImg(item.getImageList().getFirst().getImageLink())
                .itemName(item.getName())
                .talkBoxCntInfo(toTalkBoxInfoDto(item, waitingCnt, completedCnt))
                .newCnt(unCheckedCnt)
                .build();
    }

    public static ItemResponseDto.TalkBoxOpenedListDto toTalkBoxOpenedListDto(List<Item> itemList, Map<Long, Integer> waitingCntMap, Map<Long, Integer> completedCntMap, Map<Long, Integer> unCheckedCntMap) {
        List<ItemResponseDto.TalkBoxOpenedDto> itemDtoList = itemList.stream()
                .map(item -> {
                    Long itemId = item.getId();
                    Integer waitingCnt = waitingCntMap.getOrDefault(itemId, 0);
                    Integer completedCnt = completedCntMap.getOrDefault(itemId, 0);
                    Integer unCheckedCnt = unCheckedCntMap.getOrDefault(itemId, 0);
                    return toTalkBoxOpenedDto(item, waitingCnt, completedCnt, unCheckedCnt);
                })
                .toList();

        return ItemResponseDto.TalkBoxOpenedListDto.builder()
                .cnt(itemList.size())
                .talkBoxOpenedDtoList(itemDtoList)
                .build();
    }

    public static ItemResponseDto.HomeItemViewDto toHomeItemViewDto(Item item, Boolean liked) {
        Member member = item.getSeller().getMember();

        return ItemResponseDto.HomeItemViewDto.builder()
                .sellerProfileImg(member.getProfileImg())
                .sellerUsername(member.getUsername())
                .itemId(item.getId())
                .itemName(item.getName())
                .itemMainImg(item.getImageList().getFirst().getImageLink())
                .itemPeriod(item.getItemPeriod())
                .endDate(item.getEndDate())
                .tagline(item.getTagline())
                .currentStatus(item.getItemStatus())
                .liked(liked)
                .build();
    }

    public static ItemResponseDto.HomeItemViewPageDto toHomeItemViewPageDto(Page<Item> itemPage, List<Long> likeItems) {
        List<Long> safeLikeItems = (likeItems != null) ? likeItems : Collections.emptyList();

        List<ItemResponseDto.HomeItemViewDto> itemDtoList = itemPage != null
                ? itemPage.stream()
                .map(item -> toHomeItemViewDto(item, safeLikeItems.contains(item.getId())))
                .toList()
                : Collections.emptyList();

        return ItemResponseDto.HomeItemViewPageDto.builder()
                .itemPreviewList(itemDtoList)
                .listSize(itemDtoList.size())
                .totalPage(itemPage == null ? 0: itemPage.getTotalPages())
                .totalElements(itemPage == null ? 0: itemPage.getTotalElements())
                .isFirst(itemPage == null || itemPage.isFirst())
                .isLast(itemPage == null || itemPage.isLast())
                .build();
    }
}
