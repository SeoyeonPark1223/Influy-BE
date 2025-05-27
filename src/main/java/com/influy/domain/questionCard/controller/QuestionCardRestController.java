package com.influy.domain.questionCard.controller;

import com.influy.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "셀러 FAQ 카드", description = "셀러 FAQ 카드 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("seller/{sellerId}/items/{itemId}")
public class QuestionCardRestController {
    @GetMapping("/faq-categories/{faqCategoryId}/question-cards")
    @Operation(summary = "개별 상품의 faq 카테고리별 질문 카드 리스트 조회 ")
    public ApiResponse<QuestionCardResponseDto.PageDto> getPage(@PathVariable("sellerId") Long sellerId,
                                                                @PathVariable("itemId") Long itemId,
                                                                @PathVariable("faqCategoryId") Long faqCategoryId) {

    }
}
