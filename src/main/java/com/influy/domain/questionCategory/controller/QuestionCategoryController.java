package com.influy.domain.questionCategory.controller;

import com.influy.domain.faqCategory.converter.FaqCategoryConverter;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.questionCategory.converter.QuestionCategoryConverter;
import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDto;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDto;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.service.QuestionCategoryService;
import com.influy.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("seller/items/{itemId}/questions/categories")//로그인 구현 시 sellerID 삭제
@RequiredArgsConstructor
@Tag(name ="질문 카테고리")
public class QuestionCategoryController {

    private final QuestionCategoryService questionCategoryService;

    @PostMapping
    @Operation(summary = "질문 카테고리 추가",description = "특정 아이템에 질문 카테고리를 추가합니다")
    public ApiResponse<QuestionCategoryResponseDto.General> createCategory(@PathVariable("itemId") Long itemId,
                                                                           @RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                           @RequestBody QuestionCategoryRequestDto.Create request) {

        QuestionCategory category = questionCategoryService.createCategory(sellerId,itemId,request);
        QuestionCategoryResponseDto.General body = QuestionCategoryConverter.toGeneralDTO(category,0,0);

        return ApiResponse.onSuccess(body);
    }

    //질문 카테고리 리스트+질문 2개 조회
    @GetMapping
    @Operation(summary = "질문 카테고리 리스트+질문2개 조회",description = "질문 카테고리 리스트와 함께 해당 카테고리의 최신 질문 2개를 제공")
    public ApiResponse<List<QuestionCategoryResponseDto.Preview>> getCategoriesAnd2Question(@PathVariable("itemId") Long itemId,
                                                                                            @RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                                            @ParameterObject Pageable pageable) {

        Page<QuestionCategory> categories = questionCategoryService.getCategoryList(sellerId,itemId,pageable);

        List<QuestionCategoryResponseDto.Preview> body = questionCategoryService.getPreviewDTO(categories,itemId);

        return ApiResponse.onSuccess(body);
    }

    @PostMapping("seller/items/{itemId}/question-categories")
    @Operation(summary = "질문 카테고리 추가 (한번에 하나)")
    public ApiResponse<QuestionCategoryResponseDto.ViewDto> add(@RequestParam(value="sellerId", defaultValue = "1") Long sellerId,
                                                                     @PathVariable("itemId") Long itemId,
                                                                @RequestBody QuestionCategoryRequestDto.AddDto request) {
        QuestionCategory questionCategory = questionCategoryService.add(sellerId, itemId, request);
        return ApiResponse.onSuccess(QuestionCategoryConverter.toViewDto(questionCategory));
    }

}
