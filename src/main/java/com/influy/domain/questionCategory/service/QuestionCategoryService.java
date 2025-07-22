package com.influy.domain.questionCategory.service;

import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDto;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDto;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.global.jwt.CustomUserDetails;

import java.util.List;


public interface QuestionCategoryService {
    List<QuestionCategory> addAll(CustomUserDetails userDetails, Long itemId, QuestionCategoryRequestDto.AddListDto request);
    QuestionCategory update(CustomUserDetails userDetails, Long itemId, QuestionCategoryRequestDto.UpdateDto request);
    void delete(CustomUserDetails userDetails, Long itemId, QuestionCategoryRequestDto.DeleteDto request);

    QuestionCategoryResponseDto.ListWithCntDto getList(Long sellerId, Long itemId);
    List<String> generateCategory(CustomUserDetails userDetails, Long itemId);

    QuestionCategoryResponseDto.ListDto getList(Long sellerId, Long itemId);
    List<QuestionCategory> generateCategory(CustomUserDetails userDetails, Long itemId);

    QuestionCategory findByCategoryIdAndItemId(Long questionCategoryId, Long itemId);

}
