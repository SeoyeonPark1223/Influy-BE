package com.influy.domain.questionCategory.service;

import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDto;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.global.common.PageRequestDto;
import org.springframework.data.domain.Page;


public interface QuestionCategoryService {
    QuestionCategory add(Long sellerId, Long itemId, QuestionCategoryRequestDto.AddDto request);

    QuestionCategory update(Long sellerId, Long itemId, QuestionCategoryRequestDto.UpdateDto request);

    void delete(Long sellerId, Long itemId, QuestionCategoryRequestDto.DeleteDto request);

    Page<QuestionCategory> getPage(Long sellerId, Long itemId, PageRequestDto pageRequest);
}
