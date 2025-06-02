package com.influy.domain.faqCard.controller;

import com.influy.domain.faqCard.converter.FaqCardConverter;
import com.influy.domain.faqCard.dto.FaqCardResponseDto;
import com.influy.domain.faqCard.entity.FaqCard;
import com.influy.domain.faqCard.service.FaqCardService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.validation.annotation.CheckPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "셀러 FAQ 카드", description = "셀러 FAQ 카드 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("seller/{sellerId}/items/{itemId}/faq")
public class FaqCardRestController {
    private final FaqCardService faqCardService;

    @GetMapping("/question-cards")
    @Operation(summary = "개별 상품의 faq 카테고리별 질문 카드 리스트 조회 ")
    public ApiResponse<FaqCardResponseDto.PageDto> getPage(@PathVariable("sellerId") Long sellerId,
                                                           @PathVariable("itemId") Long itemId,
                                                           @RequestParam(name = "faqCategoryId") Long faqCategoryId,
                                                           @CheckPage @RequestParam(name = "page") Integer page) {
        Integer pageNumber = page - 1;
        Page<FaqCard> questionCardPage = faqCardService.getPage(sellerId, itemId, faqCategoryId, pageNumber);
        return ApiResponse.onSuccess(FaqCardConverter.toPageDto(questionCardPage));
    }
}
