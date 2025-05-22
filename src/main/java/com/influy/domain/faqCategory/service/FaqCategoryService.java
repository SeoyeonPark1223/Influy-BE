package com.influy.domain.faqCategory.service;

import com.influy.domain.faqCategory.dto.FaqCategoryRequestDto;
import com.influy.domain.faqCategory.entity.FaqCategory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FaqCategoryService {
    List<FaqCategory> addAll(Long sellerId, Long itemId, List<FaqCategoryRequestDto.AddDto> requestList);
    Page<FaqCategory> getPage(Long sellerId, Long itemId, Integer pageNumber);
    void deleteAll(Long sellerId, Long itemId, List<FaqCategoryRequestDto.DeleteDto> requestList);
    List<FaqCategory> updateAll(Long sellerId, Long itemId, List<FaqCategoryRequestDto.UpdateDto> requestList);
}
