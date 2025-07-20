package com.influy.domain.question.controller;


import com.influy.domain.question.converter.QuestionConverter;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.entity.Question;
import com.influy.domain.question.service.QuestionService;
import com.influy.domain.sellerProfile.service.SellerProfileService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="질문 관리")
@RequiredArgsConstructor
@RequestMapping("seller/items/{itemId}/questions/{questionCategoryId}")//sellerID 파라미터..어케할건지
public class QuestionController {
    private final SellerProfileService sellerService;
    private final QuestionService questionService;

    @GetMapping("")
    @Operation(summary = "각 카테고리별 전체 질문 조회", description = "답변 완료/대기 요청 따로따로 보내야함(파라미터로)")
    public ApiResponse<Object> getQuestions(@PathVariable("itemId") Long itemId,
                                            @PathVariable("questionCategoryId") Long questionCategoryId,
                                            @RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                            @RequestParam(value = "isAnswered",defaultValue = "true") Boolean isAnswered,
                                            @ParameterObject Pageable pageable) {

        //자격 검사
        sellerService.checkItemMatchSeller(itemId,sellerId);

        Page<Question> questions = questionService.getQuestionsByCategory(questionCategoryId, isAnswered, pageable);
        QuestionResponseDTO.GeneralPage body = QuestionConverter.toGeneralPageDTO(questions);

        return ApiResponse.onSuccess(body);
    }
}
