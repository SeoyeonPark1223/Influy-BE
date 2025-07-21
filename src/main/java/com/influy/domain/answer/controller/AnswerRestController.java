package com.influy.domain.answer.controller;

import com.influy.domain.answer.converter.AnswerConverter;
import com.influy.domain.answer.dto.AnswerRequestDto;
import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.domain.answer.entity.Answer;
import com.influy.domain.answer.entity.AnswerType;
import com.influy.domain.answer.service.AnswerService;
import com.influy.domain.faqCard.dto.FaqCardRequestDto;
import com.influy.domain.faqCard.dto.FaqCardResponseDto;
import com.influy.domain.item.entity.TalkBoxOpenStatus;
import com.influy.domain.sellerProfile.entity.ItemSortType;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.apiPayload.code.status.SuccessStatus;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "셀러 톡박스 답변", description = "셀러 톡박스 답변 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/items/{itemId}/talkbox")
public class AnswerRestController {
    private final AnswerService answerService;

    @GetMapping("/{questionCategoryId}/answers")
    @Operation(summary = "해당 태그를 가진 답변들 리스트 조회")
    public ApiResponse<AnswerResponseDto.AnswerTagListDto> getList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @PathVariable("itemId") Long itemId,
                                                                      @PathVariable("questionCategoryId") Long questionCategoryId,
                                                                      @RequestParam("questionTagId") Long questionTagId) {
        return ApiResponse.onSuccess(answerService.getList(userDetails, itemId, questionCategoryId, questionTagId));
    }

    @PostMapping("/{questionCategoryId}/answers")
    @Operation(summary = "일괄질문 답변 작성")
    public ApiResponse<AnswerResponseDto.AnswerCommonResultDto> createCommonAnswers(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @PathVariable("itemId") Long itemId,
                                                                      @PathVariable("questionCategoryId") Long questionCategoryId,
                                                                                    @RequestBody @Valid AnswerRequestDto.AnswerCommonDto request) {
        return ApiResponse.onSuccess(answerService.createCommonAnswers(userDetails, itemId, questionCategoryId, request));
    }

    @PostMapping("/{questionCategoryId}/questions/{questionTagId}/{questionId}/answers")
    @Operation(summary = "개별질문 답변 작성 (INDIVIDUAL, FAQ 선택)")
    public ApiResponse<AnswerResponseDto.AnswerResultDto> createIndividualAnswer(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                 @PathVariable("itemId") Long itemId,
                                                                                 @PathVariable("questionCategoryId") Long questionCategoryId,
                                                                                 @PathVariable("questionTagId") Long questionTagId,
                                                                                 @PathVariable("questionId") Long questionId,
                                                                                 @RequestBody @Valid AnswerRequestDto.AnswerIndividualDto request,
                                                                                 @RequestParam(name = "answerType", defaultValue = "INDIVIDUAL") AnswerType answerType) {
        Answer answer = answerService.createIndividualAnswer(userDetails, itemId, questionCategoryId, questionTagId, questionId, request, answerType);
        return ApiResponse.onSuccess(AnswerConverter.toAnswerResultDto(answer));
    }

    @DeleteMapping("/{questionCategoryId}/questions")
    @Operation(summary = "선택한 질문들 삭제 (여러개 가능, 질문 객체 삭제가 아닌 isHidden 처리)")
    public ApiResponse<AnswerResponseDto.DeleteResultDto> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @PathVariable("itemId") Long itemId,
                                                                 @PathVariable("questionCategoryId") Long questionCategoryId,
                                                                 @RequestBody @Valid AnswerRequestDto.DeleteDto request) {
        return ApiResponse.onSuccess(answerService.delete(userDetails, itemId, questionCategoryId, request));
    }

//    @GetMapping("/{questionCategoryId}/questions/{questionTagId}/{questionId}")
//    @Operation(summary = "개별 질문 + 답변들 조회 (아직 구현 x)")
//    public ApiResponse<AnswerResponseDto.QnAListDto> viewQnA(@AuthenticationPrincipal CustomUserDetails userDetails,
//                                                             @PathVariable("itemId") Long itemId,
//                                                             @PathVariable("questionCategoryId") Long questionCategoryId,
//                                                             @PathVariable("questionTagId") Long questionTagId,
//                                                             @PathVariable("questionId") Long questionId) {
//        return ApiResponse.onSuccess(SuccessStatus._OK);
//    }

    @PostMapping("/{questionCategoryId}/answers/{answerId}/to-faq")
    @Operation(summary = "답변 기반 faq 카드 등록")
    public ApiResponse<AnswerResponseDto.AnswerToFaqResultDto> answerToFaq(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                         @PathVariable("itemId") Long itemId,
                                                                         @PathVariable("questionCategoryId") Long questionCategoryId,
                                                                           @PathVariable("answerId") Long answerId,
                                                                         @RequestBody @Valid AnswerRequestDto.QuestionToFaqDto request) {
        return ApiResponse.onSuccess(answerService.answerToFaq(userDetails, itemId, questionCategoryId, answerId, request));
    }

    @PostMapping("/answers/{answerId}/view-faq")
    @Operation(summary = "답변 id로 faq 카드 조회")
    public ApiResponse<FaqCardResponseDto.FaqCardDto> viewFaqWithAnswerId(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                          @PathVariable("itemId") Long itemId,
                                                                          @PathVariable("answerId") Long answerId) {
        return ApiResponse.onSuccess(answerService.viewFaqWithAnswerId(userDetails, itemId, answerId));
    }

    @PostMapping("/open-status")
    @Operation(summary = "톡박스 오픈 여부 수정")
    public ApiResponse<AnswerResponseDto.TalkBoxOpenStatusDto> changeOpenStatus(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                          @PathVariable("itemId") Long itemId,
                                                                          @RequestParam(name = "openStatus", defaultValue = "OPENED") TalkBoxOpenStatus openStatus) {
        return ApiResponse.onSuccess(answerService.changeOpenStatus(userDetails, itemId, openStatus));
    }
}
