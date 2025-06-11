package com.influy.domain.question.controller;


import com.influy.domain.question.entity.Question;
import com.influy.domain.question.service.QuestionService;
import com.influy.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("seller/items/{itemId}/questions/{questionCategoryId}")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("")
    @Operation(summary = "각 카테고리별 전체 질문 조회", description = "답변 완료/대기 요청 따로따로 보내야함(파라미터로)")
    public ApiResponse<Object> getQuestions(@PathVariable("itemId") Long itemId,
                                            @PathVariable("questionCategoryId") Long questionCategoryId,
                                            @RequestParam(value = "isAnswered",defaultValue = "true") Boolean isAnswered,
                                            @ParameterObject Pageable pageable) {

        Page<Question> questions = questionService.getQuestionsByCategory(questionCategoryId, isAnswered, pageable);

        return ApiResponse.onSuccess(null);

    }
}
