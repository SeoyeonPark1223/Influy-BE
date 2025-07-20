package com.influy.domain.answer.controller;

import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.apiPayload.code.status.SuccessStatus;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "셀러 톡박스 답변", description = "셀러 톡박스 답변 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/items/{itemId}/talkbox/{questionCategoryId}")
public class AnswerRestController {
    @GetMapping("/answers")
    @Operation(summary = "해당 태그를 가진 답변들 리스트 조회")
    public ApiResponse<AnswerResponseDto.AnswerListDto> getAnswerList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @PathVariable("itemId") Long itemId,
                                                                      @PathVariable("questionCategoryId") Long questionCategoryId,
                                                                      @RequestParam("questionTagId") Long questionTagId) {
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }
}
