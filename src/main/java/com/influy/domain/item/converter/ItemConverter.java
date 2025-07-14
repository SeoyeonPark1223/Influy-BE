package com.influy.domain.item.converter;

import com.influy.domain.item.dto.ItemRequestDto;
import com.influy.domain.item.dto.ItemResponseDto;
import com.influy.domain.item.entity.Item;
import com.influy.domain.image.entity.Image;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
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

    public static ItemResponseDto.DetailPreviewDto toDetailPreviewDto(Item item, boolean liked) {
        return ItemResponseDto.DetailPreviewDto.builder()
                .itemId(item.getId())
                .MainImg(item.getImageList().get(0).getImageLink())
                .itemPeriod(item.getItemPeriod())
                .itemName(item.getName())
                .sellerName(item.getSeller().getMember().getUsername())
                .startDate(item.getStartDate())
                .endDate(item.getEndDate())
                .tagline(item.getTagline())
                .currentStatus(item.getItemStatus())
                .liked(liked)
                .build();
    }

    public static ItemResponseDto.DetailPreviewPageDto toDetailPreviewPageDto(Page<Item> itemPage, List<Long> likeItems) {
        List<Long> safeLikeItems = (likeItems != null) ? likeItems : Collections.emptyList();

        List<ItemResponseDto.DetailPreviewDto> itemPreviewList = itemPage.stream()
                .map(item -> toDetailPreviewDto(item, safeLikeItems.contains(item.getId())))
                .toList();

        return ItemResponseDto.DetailPreviewPageDto.builder()
                .itemPreviewList(itemPreviewList)
                .listSize(itemPage.getContent().size())
                .totalPage(itemPage.getTotalPages())
                .totalElements(itemPage.getTotalElements())
                .isFirst(itemPage.isFirst())
                .isLast(itemPage.isLast())
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
}
