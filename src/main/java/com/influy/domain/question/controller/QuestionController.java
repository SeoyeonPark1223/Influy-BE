package com.influy.domain.question.controller;


import com.influy.domain.item.service.ItemService;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.domain.member.service.MemberService;
import com.influy.domain.question.converter.QuestionConverter;
import com.influy.domain.question.dto.QuestionRequestDTO;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.entity.Question;
import com.influy.domain.question.service.QuestionService;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.service.SellerProfileService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Tag(name="질문 관리")
@RequiredArgsConstructor
public class QuestionController {
    private final MemberService memberService;
    private final SellerProfileService sellerService;
    private final QuestionService questionService;
    private final ItemService itemService;

    @GetMapping("seller/items/{itemId}/questions/{questionCategoryId}")
    @Operation(summary = "각 카테고리별 전체 질문 조회", description = "답변 완료/대기 요청 따로따로 보내야함(파라미터로)")
    public ApiResponse<QuestionResponseDTO.GeneralPage> getQuestions(@PathVariable("itemId") Long itemId,
                                                                     @PathVariable("questionCategoryId") Long questionCategoryId,
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @RequestParam(value = "isAnswered",defaultValue = "true") Boolean isAnswered,
                                                                     @ParameterObject Pageable pageable) {

        Member member = memberService.findById(userDetails.getId());

        //자격 검사
        if(member.getRole().equals(MemberRole.SELLER)){
            sellerService.checkItemMatchSeller(itemId, Objects.requireNonNull(member.getSellerProfile()).getId());
        }else{
            throw new GeneralException(ErrorStatus.FORBIDDEN);
        }


        Page<Question> questions = questionService.getQuestionsByCategory(questionCategoryId, isAnswered, pageable);
        QuestionResponseDTO.GeneralPage body = QuestionConverter.toGeneralPageDTO(questions);

        return ApiResponse.onSuccess(body);


    }

    @PostMapping("user/items/{sellerId}/questions/{questionCategoryId}")
    @Operation(summary = "질문 작성", description = "질문 작성 API")
    public ApiResponse<Object> postQuestion(@RequestBody QuestionRequestDTO.Create request, @AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable("sellerId") Long sellerId, @PathVariable("questionCategoryId") Long questionCategoryId){

        Member member = memberService.findById(userDetails.getId());
        SellerProfile seller = sellerService.getSellerProfile(sellerId);

        Question question = questionService.createQuestion(member, seller, questionCategoryId,request.getContent());



        return null;
    }

}
