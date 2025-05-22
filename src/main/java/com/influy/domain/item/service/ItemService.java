package com.influy.domain.item.service;

import com.influy.domain.item.dto.ItemRequestDto;
import com.influy.domain.item.entity.Item;
import com.influy.domain.seller.entity.ItemSortType;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ItemService {
    Item createItem(Long sellerId, ItemRequestDto.DetailDto request);
    Item getDetail(Long sellerId, Long itemId);
    void deleteItem(Long sellerId, Long itemId);
    Item updateItem(Long sellerId, Long itemId, ItemRequestDto.DetailDto request);
    Item setAccess(Long sellerId, Long itemId, ItemRequestDto.AccessDto request);
    Item setStatus(Long sellerId, Long itemId, ItemRequestDto.StatusDto request);
    Integer getCount(Long sellerId, Boolean isArchived);
    Page<Item> getDetailPreviewPage(Long sellerId, Boolean isArchived, Integer pageNumber, ItemSortType sortType);
}
