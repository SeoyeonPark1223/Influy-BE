package com.influy.domain.questionCategory.controller;

import com.influy.domain.questionCategory.converter.QuestionCategoryConverter;
import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDto;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDto;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.service.QuestionCategoryService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.common.PageRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Tag(name ="질문 카테고리")
public class QuestionCategoryController {

    private final QuestionCategoryService questionCategoryService;

    @PostMapping("seller/items/{itemId}/question-categories")
    @Operation(summary = "질문 카테고리 추가 (한번에 하나)")
    public ApiResponse<QuestionCategoryResponseDto.ViewDto> add(@RequestParam(value="sellerId", defaultValue = "1") Long sellerId,
                                                                     @PathVariable("itemId") Long itemId,
                                                                @RequestBody QuestionCategoryRequestDto.AddDto request) {
        QuestionCategory questionCategory = questionCategoryService.add(sellerId, itemId, request);
        return ApiResponse.onSuccess(QuestionCategoryConverter.toViewDto(questionCategory, 0, 0, 0));
    }

    @PatchMapping("seller/items/{itemId}/question-categories")
    @Operation(summary = "질문 카테고리 수정 (한번에 하나)")
    public ApiResponse<QuestionCategoryResponseDto.ViewDto> update(@RequestParam(value="sellerId", defaultValue = "1") Long sellerId,
                                                                     @PathVariable("itemId") Long itemId,
                                                                     @RequestBody QuestionCategoryRequestDto.UpdateDto request) {
        QuestionCategory questionCategory = questionCategoryService.update(sellerId, itemId, request);
        return ApiResponse.onSuccess(QuestionCategoryConverter.toViewDto(questionCategory, 0, 0, 0));
    }

    @DeleteMapping("seller/items/{itemId}/question-categories")
    @Operation(summary = "질문 카테고리 삭제 (한번에 하나)")
    public ApiResponse<QuestionCategoryResponseDto.DeleteResultDto> delete(@RequestParam(value="sellerId", defaultValue = "1") Long sellerId,
                                                                           @PathVariable("itemId") Long itemId,
                                                                           @RequestBody QuestionCategoryRequestDto.DeleteDto request) {
        questionCategoryService.delete(sellerId, itemId, request);
        return ApiResponse.onSuccess(QuestionCategoryConverter.toDeleteResultDto(request.getId()));
    }

    @GetMapping("seller/{sellerId}/items/{itemId}/question-categories")
    @Operation(summary = "질문 카테고리 리스트 조회 (질문 많은 순)")
    public ApiResponse<QuestionCategoryResponseDto.PageDto> getPage(@PathVariable("sellerId") Long sellerId,
                                                                @PathVariable("itemId") Long itemId,
                                                                    @Valid @ParameterObject PageRequestDto pageRequest) {
        Page<QuestionCategory> questionCategoryPage = questionCategoryService.getPage(sellerId, itemId, pageRequest);
        return ApiResponse.onSuccess(QuestionCategoryConverter.toPageDto(questionCategoryPage));
    }
}
