package com.influy.domain.faqCategory.controller;

import com.influy.domain.faqCategory.dto.FaqCategoryRequestDto;
import com.influy.domain.faqCategory.dto.FaqCategoryResponseDto;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.faqCategory.service.FaqCategoryService;
import com.influy.domain.faqCategory.converter.FaqCategoryConverter;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.common.PageRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "셀러 아이템 FAQ", description = "셀러 아이템 FAQ 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class FaqCategoryRestController {
    private final FaqCategoryService faqCategoryService;

    @PostMapping("/items/{itemId}/faq-categories")
    @Operation(summary = "개별 상품의 faq 카테고리 추가 (한번에 하나)")
    public ApiResponse<FaqCategoryResponseDto.ViewDto> add(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                    @PathVariable("itemId") Long itemId,
                                                                    @RequestBody FaqCategoryRequestDto.AddDto request) {
        FaqCategory faqCategory = faqCategoryService.add(sellerId, itemId, request);
        return ApiResponse.onSuccess(FaqCategoryConverter.toViewDto(faqCategory));
    }

    @GetMapping("/{sellerId}/items/{itemId}/faq-categories")
    @Operation(summary = "개별 상품의 faq 카테고리 리스트 조회 (순서 기준 정렬)")
    public ApiResponse<FaqCategoryResponseDto.ListDto> getList (@PathVariable("sellerId") Long sellerId,
                                                                @PathVariable("itemId") Long itemId) {
        List<FaqCategory> faqCategoryList = faqCategoryService.getList(sellerId, itemId);
        return ApiResponse.onSuccess(FaqCategoryConverter.toListDto(faqCategoryList));
    }

    @DeleteMapping("/items/{itemId}/faq-categories")
    @Operation(summary = "개별 상품의 faq 카테고리 삭제 (한번에 하나)")
    public ApiResponse<FaqCategoryResponseDto.DeleteResultDto> delete(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                          @PathVariable("itemId") Long itemId,
                                                                          @RequestBody @Valid FaqCategoryRequestDto.DeleteDto request) {
        faqCategoryService.delete(sellerId, itemId, request);
        return ApiResponse.onSuccess(FaqCategoryConverter.toDeleteResultDto(request));
    }

    @PatchMapping("/items/{itemId}/faq-categories")
    @Operation(summary = "개별 상품의 faq 카테고리 이름 수정 (한번에 하나)")
    public ApiResponse<FaqCategoryResponseDto.ViewDto> update(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                              @PathVariable("itemId") Long itemId,
                                                                              @RequestBody @Valid FaqCategoryRequestDto.UpdateDto request) {
        FaqCategory faqCategory = faqCategoryService.update(sellerId, itemId, request);
        return ApiResponse.onSuccess(FaqCategoryConverter.toViewDto(faqCategory));
    }

    @PatchMapping("/items/{itemId}/faq-categories/order")
    @Operation(summary = "개별 상품의 faq 카테고리 순서 수정 (한번에 여러개 가능)")
    public ApiResponse<FaqCategoryResponseDto.UpdateOrderResultDto> updateOrderAll(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                   @PathVariable("itemId") Long itemId,
                                                                   @RequestBody @Valid FaqCategoryRequestDto.UpdateOrderDto request) {
        List<FaqCategory> faqCategoryList = faqCategoryService.updateOrderAll(sellerId, itemId, request);
        return ApiResponse.onSuccess(FaqCategoryConverter.toUpdateOrderResultDto(faqCategoryList));
    }
}

