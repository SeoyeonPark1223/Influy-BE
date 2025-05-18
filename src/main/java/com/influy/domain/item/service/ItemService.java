package com.influy.domain.item.service;

import com.influy.domain.item.dto.ItemRequestDto;
import com.influy.domain.item.entity.Item;
import jakarta.validation.Valid;

import java.util.List;

public interface ItemService {
    Item createItem(Long sellerId, ItemRequestDto.DetailDto request);
    List<Item> getDetailPreviewList(Long sellerId);
    Item getDetail(Long sellerId, Long itemId);
    void deleteItem(Long sellerId, Long itemId);
    Item updateItem(Long sellerId, Long itemId, ItemRequestDto.DetailDto request);
    Item setAccess(Long sellerId, Long itemId, ItemRequestDto.AccessDto request);
    Item setStatus(Long sellerId, Long itemId, ItemRequestDto.StatusDto request);
    List<Item> getArchivedDetailPreviewList(Long sellerId);
}
