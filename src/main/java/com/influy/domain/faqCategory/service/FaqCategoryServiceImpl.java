package com.influy.domain.faqCategory.service;

import com.influy.domain.faqCategory.dto.FaqCategoryRequestDTO;
import com.influy.domain.faqCategory.entity.FaqCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqCategoryServiceImpl implements FaqCategoryService {
    @Override
    public List<FaqCategory> addAll(Long sellerId, Long itemId, FaqCategoryRequestDTO.AddDto request) {
        return List.of();
    }

    @Override
    public Page<FaqCategory> getPage(Long sellerId, Long itemId, Integer pageNumber) {
        return null;
    }

    @Override
    public void delete(Long sellerId, Long itemId, Long faqCategoryId) {

    }

    @Override
    public List<FaqCategory> updateAll(Long sellerId, Long itemId, Long faqCategoryId, FaqCategoryRequestDTO.UpdateDto request) {
        return List.of();
    }
}
