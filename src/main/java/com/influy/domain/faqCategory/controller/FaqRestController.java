package com.influy.domain.faqCategory.controller;

import com.influy.domain.faqCategory.dto.FaqCategoryRequestDto;
import com.influy.domain.faqCategory.dto.FaqCategoryResponseDto;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.faqCategory.service.FaqCategoryService;
import com.influy.domain.faqCategory.converter.FaqCategoryConverter;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.common.PageRequestDto;
import com.influy.global.validation.annotation.CheckPage;
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
public class FaqRestController {
    private final FaqCategoryService faqCategoryService;

    @PostMapping("/items/{itemId}/faq-categories")
    @Operation(summary = "개별 상품의 faq 카테고리 추가 (한번에 여러개 가능)")
    public ApiResponse<FaqCategoryResponseDto.AddResultDto> addAll (@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                    @PathVariable("itemId") Long itemId,
                                                                    @RequestBody List<FaqCategoryRequestDto.AddDto> requestList) {
        List<FaqCategory> faqCategoryList = faqCategoryService.addAll(sellerId, itemId, requestList);
        return ApiResponse.onSuccess(FaqCategoryConverter.toAddResultDto(faqCategoryList));
    }

    @GetMapping("/{sellerId}/items/{itemId}/faq-categories")
    @Operation(summary = "개별 상품의 faq 카테고리 리스트 조회 (순서 기준 정렬)")
    public ApiResponse<FaqCategoryResponseDto.PageDto> getPage (@PathVariable("sellerId") Long sellerId,
                                                                @PathVariable("itemId") Long itemId,
                                                                @Valid @ParameterObject PageRequestDto pageRequest) {
        Page<FaqCategory> faqCategoryPage = faqCategoryService.getPage(sellerId, itemId, pageRequest);
        return ApiResponse.onSuccess(FaqCategoryConverter.toPageDto(faqCategoryPage));
    }

    @DeleteMapping("/items/{itemId}/faq-categories")
    @Operation(summary = "개별 상품의 faq 카테고리 삭제")
    public ApiResponse<FaqCategoryResponseDto.DeleteResultDto> deleteAll (@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                          @PathVariable("itemId") Long itemId,
                                                                          @RequestBody @Valid List<FaqCategoryRequestDto.DeleteDto> requestList) {
        faqCategoryService.deleteAll(sellerId, itemId, requestList);
        return ApiResponse.onSuccess(FaqCategoryConverter.toDeleteResultDto(requestList));
    }

    @PatchMapping("/items/{itemId}/faq-categories")
    @Operation(summary = "개별 상품의 faq 카테고리 수정")
    public ApiResponse<FaqCategoryResponseDto.UpdateResultDto> updateAll(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                   @PathVariable("itemId") Long itemId,
                                                                   @RequestBody @Valid List<FaqCategoryRequestDto.UpdateDto> requestList) {
        List<FaqCategory> faqCategoryList = faqCategoryService.updateAll(sellerId, itemId, requestList);
        return ApiResponse.onSuccess(FaqCategoryConverter.toUpdateResultDto(faqCategoryList));
    }
}
