package com.influy.domain.questionCategory.service;

import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDto;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDto;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.global.common.PageRequestDto;
import org.springframework.data.domain.Page;

import java.util.List;


public interface QuestionCategoryService {
    QuestionCategory add(Long sellerId, Long itemId, QuestionCategoryRequestDto.AddDto request);

    QuestionCategory update(Long sellerId, Long itemId, QuestionCategoryRequestDto.UpdateDto request);

    void delete(Long sellerId, Long itemId, QuestionCategoryRequestDto.DeleteDto request);

    QuestionCategoryResponseDto.ListDto getList(Long sellerId, Long itemId);

    List<QuestionCategory> generateCategory(Long sellerId, Long itemId);
}
