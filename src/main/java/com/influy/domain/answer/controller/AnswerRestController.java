package com.influy.domain.answer.controller;

import com.influy.domain.answer.converter.AnswerConverter;
import com.influy.domain.answer.dto.AnswerRequestDto;
import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.domain.answer.entity.Answer;
import com.influy.domain.answer.service.AnswerService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.apiPayload.code.status.SuccessStatus;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "셀러 톡박스 답변", description = "셀러 톡박스 답변 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/items/{itemId}/talkbox/{questionCategoryId}")
public class AnswerRestController {
    private final AnswerService answerService;

    @GetMapping("/answers")
    @Operation(summary = "해당 태그를 가진 답변들 리스트 조회")
    public ApiResponse<AnswerResponseDto.AnswerListDto> getList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @PathVariable("itemId") Long itemId,
                                                                      @PathVariable("questionCategoryId") Long questionCategoryId,
                                                                      @RequestParam("questionTagId") Long questionTagId) {
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @PostMapping("/answers")
    @Operation(summary = "일괄질문 답변 작성")
    public ApiResponse<AnswerResponseDto.AnswerResultDto> createCommonAnswers(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @PathVariable("itemId") Long itemId,
                                                                      @PathVariable("questionCategoryId") Long questionCategoryId) {
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @PostMapping("/questions/{questionTagId}/{questionId}/answers")
    @Operation(summary = "개별질문 답변 작성")
    public ApiResponse<AnswerResponseDto.AnswerResultDto> createIndividualAnswer(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                 @PathVariable("itemId") Long itemId,
                                                                                 @PathVariable("questionCategoryId") Long questionCategoryId,
                                                                                 @PathVariable("questionTagId") Long questionTagId,
                                                                                 @PathVariable("questionId") Long questionId,
                                                                                 @RequestBody @Valid AnswerRequestDto.AnswerIndividualDto request) {
        Answer answer = answerService.createIndividualAnswer(userDetails, itemId, questionCategoryId, questionTagId, questionId, request);
        return ApiResponse.onSuccess(AnswerConverter.toAnswerResultDto(answer));
    }

    @PostMapping("/questions/{questionTagId}/{questionId}/answers-faq")
    @Operation(summary = "개별질문을 faq로 등록")
    public ApiResponse<AnswerResponseDto.AnswerResultDto> toFaq(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @PathVariable("itemId") Long itemId,
                                                                @PathVariable("questionCategoryId") Long questionCategoryId,
                                                                @PathVariable("questionTagId") Long questionTagId,
                                                                @PathVariable("questionId") Long questionId) {
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @DeleteMapping("/questions")
    @Operation(summary = "선택한 질문들 삭제 (여러개 가능)")
    public ApiResponse<AnswerResponseDto.DeleteResultDto> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @PathVariable("itemId") Long itemId,
                                                                 @PathVariable("questionCategoryId") Long questionCategoryId) {
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @GetMapping("/questions/{questionTagId}/{questionId}")
    @Operation(summary = "개별 질문 + 답변들 조회")
    public ApiResponse<AnswerResponseDto.QnAListDto> viewQnA(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @PathVariable("itemId") Long itemId,
                                                             @PathVariable("questionCategoryId") Long questionCategoryId,
                                                             @PathVariable("questionTagId") Long questionTagId,
                                                             @PathVariable("questionId") Long questionId) {
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }
}
