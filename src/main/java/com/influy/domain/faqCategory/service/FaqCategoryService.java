package com.influy.domain.faqCategory.service;

import com.influy.domain.faqCategory.dto.FaqCategoryRequestDTO;
import com.influy.domain.faqCategory.entity.FaqCategory;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FaqCategoryService {
    List<FaqCategory> addAll(Long sellerId, Long itemId, FaqCategoryRequestDTO.AddDto request);
    Page<FaqCategory> getPage(Long sellerId, Long itemId, Integer pageNumber);
    void delete(Long sellerId, Long itemId, Long faqCategoryId);
    List<FaqCategory> updateAll(Long sellerId, Long itemId, Long faqCategoryId, FaqCategoryRequestDTO.UpdateDto request);
}
