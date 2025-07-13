package com.influy.domain.questionCategory.service;

import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDto;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDto;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionCategoryService {
    QuestionCategory createCategory(Long sellerId, Long itemId, QuestionCategoryRequestDto.AddDto request);

    Page<QuestionCategory> getCategoryList(Long sellerId, Long itemId, Pageable pageable);

    List<QuestionCategoryResponseDto.Preview> getPreviewDTO(Page<QuestionCategory> categories, Long itemId);

    QuestionCategory add(Long sellerId, Long itemId, QuestionCategoryRequestDto.AddDto request);
}
