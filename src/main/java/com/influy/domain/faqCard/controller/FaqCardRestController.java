package com.influy.domain.faqCard.controller;

import com.influy.domain.faqCard.converter.FaqCardConverter;
import com.influy.domain.faqCard.dto.FaqCardRequestDto;
import com.influy.domain.faqCard.dto.FaqCardResponseDto;
import com.influy.domain.faqCard.entity.FaqCard;
import com.influy.domain.faqCard.service.FaqCardService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.common.PageRequestDto;
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
@RequestMapping("/seller")
public class FaqCardRestController {
    private final FaqCardService faqCardService;

    @GetMapping("/{sellerId}/items/{itemId}/faq/question-cards")
    @Operation(summary = "개별 상품의 faq 카테고리별 질문 카드 리스트 조회 ")
    public ApiResponse<FaqCardResponseDto.QuestionCardPageDto> getQuestionCardPage(@PathVariable("sellerId") Long sellerId,
                                                                       @PathVariable("itemId") Long itemId,
                                                                       @RequestParam(name = "faqCategoryId") Long faqCategoryId,
                                                                       @Valid @ParameterObject PageRequestDto pageRequest) {

        Page<FaqCard> questionCardPage = faqCardService.getFaqCardPage(sellerId, itemId, faqCategoryId, pageRequest);
        return ApiResponse.onSuccess(FaqCardConverter.toQuestionCardPageDto(questionCardPage));
    }

    @GetMapping("/{sellerId}/items/{itemId}/faq/item-info")
    @Operation(summary = "faq 카드 등록시 아이템 정보 조회")
    public ApiResponse<FaqCardResponseDto.ItemInfoDto> getItemInfo(@PathVariable("sellerId") Long sellerId,
                                                                   @PathVariable("itemId") Long itemId) {
        return ApiResponse.onSuccess(faqCardService.getItemInfo(sellerId, itemId));
    }

    @PostMapping("/items/{itemId}/faq")
    @Operation(summary = "faq 카테고리별 faq 카드 등록")
    public ApiResponse<FaqCardResponseDto.CreateResultDto> create(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                  @PathVariable("itemId") Long itemId,
                                                                  @RequestParam(name = "faqCategoryId") Long faqCategoryId,
                                                                  @RequestBody @Valid FaqCardRequestDto.CreateDto request) {
        FaqCard faqCard = faqCardService.create(sellerId, itemId, faqCategoryId, request);
        return ApiResponse.onSuccess(FaqCardConverter.toCreateResultDto(faqCard));
    }

    @GetMapping("/{sellerId}/items/{itemId}/faq/{faqCardId}")
    @Operation(summary = "각 faq 카드 조회")
    public ApiResponse<FaqCardResponseDto.FaqCardDto> getFaqCard(@PathVariable("sellerId") Long sellerId,
                                                                    @PathVariable("itemId") Long itemId,
                                                                    @PathVariable("faqCardId") Long faqCardId) {
        FaqCard faqCard = faqCardService.getAnswerCard(sellerId, itemId, faqCardId);
        return ApiResponse.onSuccess(FaqCardConverter.toFaqCardDto(faqCard));
    }

    @GetMapping("/{sellerId}/items/{itemId}/faq/faq-cards")
    @Operation(summary = "faq 카테고리별 faq 카드 리스트 조회")
    public ApiResponse<FaqCardResponseDto.FaqCardPageDto> getFaqCardPage(@PathVariable("sellerId") Long sellerId,
                                                                    @PathVariable("itemId") Long itemId,
                                                                    @RequestParam(name = "faqCategoryId") Long faqCategoryId,
                                                                    @Valid @ParameterObject PageRequestDto pageRequest) {
        Page<FaqCard> faqCardPage = faqCardService.getFaqCardPage(sellerId, itemId, faqCategoryId, pageRequest);
        return ApiResponse.onSuccess(FaqCardConverter.toFaqCardPageDto(faqCardPage));
    }

    @PatchMapping("/items/{itemId}/faq/{faqCardId}")
    @Operation(summary = "각 faq 카드 수정")
    public ApiResponse<FaqCardResponseDto.UpdateResultDto> update(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                  @PathVariable("itemId") Long itemId,
                                                                  @PathVariable("faqCardId") Long faqCardId,
                                                                  @RequestBody @Valid FaqCardRequestDto.UpdateDto request) {
        FaqCard faqCard = faqCardService.update(sellerId, itemId, faqCardId, request);
        return ApiResponse.onSuccess(FaqCardConverter.toUpdateResultDto(faqCard));
    }

    @PatchMapping("/items/{itemId}/faq/{faqCardId}/pin")
    @Operation(summary = "각 faq 카드 상단 고정 여부 수정")
    public ApiResponse<FaqCardResponseDto.QuestionCardDto> pinUpdate(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                     @PathVariable("itemId") Long itemId,
                                                                     @PathVariable("faqCardId") Long faqCardId,
                                                                     @RequestParam(name = "isPinned", defaultValue = "false") boolean isPinned) {
        FaqCard faqCard = faqCardService.pinUpdate(sellerId, itemId, faqCardId, isPinned);
        return ApiResponse.onSuccess(FaqCardConverter.toQuestionCardDto(faqCard));
    }

    @DeleteMapping("/items/{itemId}/faq/{faqCardId}")
    @Operation(summary = "각 faq 카드 삭제")
    public ApiResponse<FaqCardResponseDto.DeleteResultDto> delete(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                  @PathVariable("itemId") Long itemId,
                                                                  @PathVariable("faqCardId") Long faqCardId) {
        faqCardService.delete(sellerId, itemId, faqCardId);
        return ApiResponse.onSuccess(FaqCardConverter.toDeleteResultDto(faqCardId));
    }
}
