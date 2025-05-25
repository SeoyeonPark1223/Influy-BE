package com.influy.domain.questionCategory.service;

import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDTO;
import com.influy.domain.questionCategory.entity.QuestionCategory;

public interface QuestionCategoryService {
    QuestionCategory createCategory(Long sellerId, Long itemId, QuestionCategoryRequestDTO.Create request);
}
