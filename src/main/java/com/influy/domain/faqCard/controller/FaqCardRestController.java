package com.influy.domain.faqCard.controller;

import com.influy.domain.faqCard.converter.FaqCardConverter;
import com.influy.domain.faqCard.dto.FaqCardRequestDto;
import com.influy.domain.faqCard.dto.FaqCardResponseDto;
import com.influy.domain.faqCard.entity.FaqCard;
import com.influy.domain.faqCard.service.FaqCardService;
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

@Tag(name = "셀러 FAQ 카드", description = "셀러 FAQ 카드 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("seller/items/{itemId}/faq")
public class FaqCardRestController {
    private final FaqCardService faqCardService;

    @GetMapping("/question-cards")
    @Operation(summary = "개별 상품의 faq 카테고리별 질문 카드 리스트 조회 ")
    public ApiResponse<FaqCardResponseDto.PageDto> getPage(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                           @PathVariable("itemId") Long itemId,
                                                           @RequestParam(name = "faqCategoryId") Long faqCategoryId,
                                                           @Valid @ParameterObject PageRequestDto pageRequest) {

        Page<FaqCard> questionCardPage = faqCardService.getPage(sellerId, itemId, faqCategoryId, pageRequest);
        return ApiResponse.onSuccess(FaqCardConverter.toPageDto(questionCardPage));
    }

    @PostMapping
    @Operation(summary = "faq 카테고리별 faq 카드 등록")
    public ApiResponse<FaqCardResponseDto.CreateResultDto> create(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                  @PathVariable("itemId") Long itemId,
                                                                  @RequestParam(name = "faqCategoryId") Long faqCategoryId,
                                                                  @RequestBody @Valid FaqCardRequestDto.CreateDto request) {
        FaqCard faqCard = faqCardService.create(sellerId, itemId, faqCategoryId, request);
        return ApiResponse.onSuccess(FaqCardConverter.toCreateResultDto(faqCard));
    }

    @GetMapping("/{faqCardId}/answer-card")
    @Operation(summary = "각 faq 카드의 답변 카드 조회")
    public ApiResponse<FaqCardResponseDto.AnswerCardDto> getAnswerCard(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                       @PathVariable("itemId") Long itemId,
                                                                       @PathVariable("faqCardId") Long faqCardId) {
        FaqCard faqCard = faqCardService.getAnswerCard(sellerId, itemId, faqCardId);
        return ApiResponse.onSuccess(FaqCardConverter.toAnswerCardDto(faqCard));
    }

    @PatchMapping("/{faqCardId}")
    @Operation(summary = "각 faq 카드 수정")
    public ApiResponse<FaqCardResponseDto.UpdateResultDto> update(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                  @PathVariable("itemId") Long itemId,
                                                                  @PathVariable("faqCardId") Long faqCardId,
                                                                  @RequestBody @Valid FaqCardRequestDto.UpdateDto request) {
        FaqCard faqCard = faqCardService.update(sellerId, itemId, faqCardId, request);
        return ApiResponse.onSuccess(FaqCardConverter.toUpdateResultDto(faqCard));
    }

    @PatchMapping("/{faqCardId}/pin")
    @Operation(summary = "각 faq 카드 상단 고정 여부 수정")
    public ApiResponse<FaqCardResponseDto.QuestionCardDto> pinUpdate(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                     @PathVariable("itemId") Long itemId,
                                                                     @PathVariable("faqCardId") Long faqCardId,
                                                                     @RequestParam(name = "isPinned", defaultValue = "false") boolean isPinned) {
        FaqCard faqCard = faqCardService.pinUpdate(sellerId, itemId, faqCardId, isPinned);
        return ApiResponse.onSuccess(FaqCardConverter.toQuestionCardDto(faqCard));
    }

    @DeleteMapping("/{faqCardId}")
    @Operation(summary = "각 faq 카드 삭제")
    public ApiResponse<FaqCardResponseDto.DeleteResultDto> delete(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                  @PathVariable("itemId") Long itemId,
                                                                  @PathVariable("faqCardId") Long faqCardId) {
        faqCardService.delete(sellerId, itemId, faqCardId);
        return ApiResponse.onSuccess(FaqCardConverter.toDeleteResultDto(faqCardId));
    }
}
