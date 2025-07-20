package com.influy.domain.item.service;

import com.influy.domain.faqCard.dto.FaqCardResponseDto;
import com.influy.domain.item.dto.ItemRequestDto;
import com.influy.domain.item.dto.ItemResponseDto;
import com.influy.domain.item.entity.Item;
import com.influy.domain.sellerProfile.entity.ItemSortType;
import com.influy.global.common.PageRequestDto;
import org.springframework.data.domain.Page;

public interface ItemService {
    Item createItem(Long sellerId, ItemRequestDto.DetailDto request);
    Item getDetail(Long sellerId, Long itemId);
    void deleteItem(Long sellerId, Long itemId);
    Item updateItem(Long sellerId, Long itemId, ItemRequestDto.DetailDto request);
    Item setAccess(Long sellerId, Long itemId, ItemRequestDto.AccessDto request);
    Item setStatus(Long sellerId, Long itemId, ItemRequestDto.StatusDto request);
    Integer getCount(Long sellerId, Boolean isArchived);
    ItemResponseDto.DetailPreviewPageDto getDetailPreviewPage(Long sellerId, Boolean isArchived, PageRequestDto pageRequest, ItemSortType sortType, Boolean isOnGoing, Long memberId);
    ItemResponseDto.ItemOverviewDto getItemOverview(Long sellerId, Long itemId);
}
