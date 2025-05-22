package com.influy.domain.faqCategory.controller;

import com.influy.domain.faqCategory.dto.FaqCategoryRequestDTO;
import com.influy.domain.faqCategory.dto.FaqCategoryResponseDTO;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.faqCategory.service.FaqCategoryService;
import com.influy.domain.faqCategory.converter.FaqCategoryConverter;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.validation.annotation.CheckPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "셀러 아이템 FAQ", description = "셀러 아이템 FAQ 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/{sellerId}/items/{itemId}/faq-categories")
public class FaqRestController {
    private final FaqCategoryService faqCategoryService;

    @PostMapping
    @Operation(summary = "개별 상품의 faq 카테고리 추가 (한번에 여러개 가능)")
    public ApiResponse<FaqCategoryResponseDTO.AddResultDto> addAll (@PathVariable("sellerId") Long sellerId,
                                                                    @PathVariable("itemId") Long itemId,
                                                                    @RequestBody @Valid FaqCategoryRequestDTO.AddDto request) {
        List<FaqCategory> faqCategoryList = faqCategoryService.addAll(sellerId, itemId, request);
        return ApiResponse.onSuccess(FaqCategoryConverter.toAddResultDto(faqCategoryList));
    }

    @GetMapping
    @Operation(summary = "개별 상품의 faq 카테고리 리스트 조회")
    public ApiResponse<FaqCategoryResponseDTO.PageDto> getPage (@PathVariable("sellerId") Long sellerId,
                                                                           @PathVariable("itemId") Long itemId,
                                                                           @CheckPage @RequestParam(name = "page") Integer page) {
        Integer pageNumber = page - 1;
        Page<FaqCategory> faqCategoryPage = faqCategoryService.getPage(sellerId, itemId, pageNumber);
        return ApiResponse.onSuccess(FaqCategoryConverter.toPageDto(faqCategoryPage));
    }

    @DeleteMapping("/{faqCategoryId}")
    @Operation(summary = "개별 상품의 faq 카테고리 삭제")
    public ApiResponse<FaqCategoryResponseDTO.ResultDto> delete (@PathVariable("sellerId") Long sellerId,
                                                                 @PathVariable("itemId") Long itemId,
                                                                 @PathVariable("faqCategoryId") Long faqCategoryId) {
        faqCategoryService.delete(sellerId, itemId, faqCategoryId);
        return ApiResponse.onSuccess(FaqCategoryConverter.toResultDto(itemId, faqCategoryId));
    }

    @PatchMapping("/{faqCategoryId}")
    @Operation(summary = "개별 상품의 faq 카테고리 수정 (순서 변경 포함)")
    public ApiResponse<FaqCategoryResponseDTO.UpdateDto> updateAll(@PathVariable("sellerId") Long sellerId,
                                                                @PathVariable("itemId") Long itemId,
                                                                @PathVariable("faqCategoryId") Long faqCategoryId,
                                                                @RequestBody @Valid FaqCategoryRequestDTO.UpdateDto request) {
        List<FaqCategory> faqCategoryList = faqCategoryService.updateAll(sellerId, itemId, faqCategoryId, request);
        return ApiResponse.onSuccess(FaqCategoryConverter.toUpdateDto(faqCategoryList));
    }
}
