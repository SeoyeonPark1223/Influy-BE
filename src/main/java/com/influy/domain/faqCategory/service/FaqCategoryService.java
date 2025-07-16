package com.influy.domain.faqCategory.service;

import com.influy.domain.faqCategory.dto.FaqCategoryRequestDto;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.global.common.PageRequestDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FaqCategoryService {
    FaqCategory add(Long sellerId, Long itemId, FaqCategoryRequestDto.AddDto request);
    List<FaqCategory> getList(Long sellerId, Long itemId);
    void delete(Long sellerId, Long itemId, FaqCategoryRequestDto.DeleteDto request);
    FaqCategory update(Long sellerId, Long itemId, FaqCategoryRequestDto.UpdateDto request);
    List<FaqCategory> updateOrderAll(Long sellerId, Long itemId, FaqCategoryRequestDto.UpdateOrderDto request);
}
