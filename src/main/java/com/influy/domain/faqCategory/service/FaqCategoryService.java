package com.influy.domain.faqCategory.service;

import com.influy.domain.faqCategory.dto.FaqCategoryRequestDto;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FaqCategoryService {
    FaqCategory add(CustomUserDetails userDetails, Long itemId, FaqCategoryRequestDto.AddDto request);
    List<FaqCategory> getList(Long sellerId, Long itemId);
    void delete(CustomUserDetails userDetails, Long itemId, FaqCategoryRequestDto.DeleteDto request);
    FaqCategory update(CustomUserDetails userDetails, Long itemId, FaqCategoryRequestDto.UpdateDto request);
    List<FaqCategory> updateOrderAll(CustomUserDetails userDetails, Long itemId, FaqCategoryRequestDto.UpdateOrderDto request);
}
