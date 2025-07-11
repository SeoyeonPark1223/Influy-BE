package com.influy.domain.faqCategory.service;

import com.influy.domain.faqCategory.dto.FaqCategoryRequestDto;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.global.common.PageRequestDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FaqCategoryService {
    List<FaqCategory> addAll(Long sellerId, Long itemId, List<FaqCategoryRequestDto.AddDto> requestList);
    Page<FaqCategory> getPage(Long sellerId, Long itemId, PageRequestDto pageRequest);
    void deleteAll(Long sellerId, Long itemId, List<FaqCategoryRequestDto.DeleteDto> requestList);
    List<FaqCategory> updateOrderAll(Long sellerId, Long itemId, List<FaqCategoryRequestDto.UpdateOrderDto> requestList);
}
