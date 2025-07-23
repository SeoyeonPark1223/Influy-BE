package com.influy.domain.questionCategory.controller;

import com.influy.domain.questionCategory.converter.QuestionCategoryConverter;
import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDto;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDto;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.service.QuestionCategoryService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name ="질문 카테고리")
public class QuestionCategoryController {

    private final QuestionCategoryService questionCategoryService;

    @PostMapping("seller/items/{itemId}/question-categories/generate")
    @Operation(summary = "질문 카테고리 (대분류) ai 생성")
    public ApiResponse<QuestionCategoryResponseDto.GeneratedResultDto> generateCategory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                 @PathVariable("itemId") Long itemId) {
        List<String> questionCategoryList = questionCategoryService.generateCategory(userDetails, itemId);
        return ApiResponse.onSuccess(QuestionCategoryConverter.toGeneratedResultDto(questionCategoryList));
    }

    @PostMapping("seller/items/{itemId}/question-categories")
    @Operation(summary = "질문 카테고리 추가 (한번에 여러개)")
    public ApiResponse<QuestionCategoryResponseDto.ViewListDto> addAll(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @PathVariable("itemId") Long itemId,
                                                                @RequestBody QuestionCategoryRequestDto.AddListDto request) {
        List<QuestionCategory> questionCategoryList = questionCategoryService.addAll(userDetails, itemId, request);
        return ApiResponse.onSuccess(QuestionCategoryConverter.toViewListDto(questionCategoryList));
    }

    @GetMapping("seller/{sellerId}/items/{itemId}/question-categories")
    @Operation(summary = "질문 카테고리 리스트 조회 (질문 많은 순)", description = "미확인인 질문 개수는 질문 파트 개발 이후 수정할 예정입니다.")
    public ApiResponse<QuestionCategoryResponseDto.ListWithCntDto> getList(@PathVariable("sellerId") Long sellerId,
                                                                           @PathVariable("itemId") Long itemId) {
        return ApiResponse.onSuccess(questionCategoryService.getList(sellerId, itemId));
    }
}
