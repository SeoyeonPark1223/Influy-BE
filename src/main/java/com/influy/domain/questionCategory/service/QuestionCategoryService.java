package com.influy.domain.questionCategory.service;

import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDTO;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDTO;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionCategoryService {
    QuestionCategory createCategory(Long sellerId, Long itemId, QuestionCategoryRequestDTO.Create request);

    Page<QuestionCategory> getCategoryList(Long sellerId, Long itemId, Pageable pageable);

    List<QuestionCategoryResponseDTO.Preview> getPreviewDTO(Page<QuestionCategory> categories, Long itemId);
}
