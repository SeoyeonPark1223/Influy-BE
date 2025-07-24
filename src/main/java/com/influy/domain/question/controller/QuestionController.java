package com.influy.domain.question.controller;


import com.influy.domain.item.entity.Item;
import com.influy.domain.item.service.ItemService;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.domain.member.service.MemberService;
import com.influy.domain.question.converter.QuestionConverter;
import com.influy.domain.question.dto.QuestionRequestDTO;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.dto.jpql.QuestionJPQLResult;
import com.influy.domain.question.entity.Question;
import com.influy.domain.question.service.QuestionService;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.service.QuestionCategoryService;
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

import java.util.Map;

@RestController
@Tag(name="질문 관리")
@RequiredArgsConstructor
public class QuestionController {
    private final MemberService memberService;
    private final SellerProfileService sellerService;
    private final QuestionService questionService;
    private final QuestionCategoryService questionCategoryService;
    private final ItemService itemService;

    @GetMapping("seller/item/talkbox/{questionTagId}/questions")
    @Operation(summary = "각 태그(소분류)별 전체 질문 조회", description = "답변 완료/대기 요청 따로따로 보내야함(파라미터로)")
    public ApiResponse<QuestionResponseDTO.GeneralPage> getQuestions(@PathVariable("questionTagId") Long questionTagId,
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @RequestParam(value = "isAnswered",defaultValue = "true") Boolean isAnswered,
                                                                     @ParameterObject Pageable pageable) {

        SellerProfile seller = memberService.checkSeller(userDetails);
        sellerService.checkQuestionOwner(questionTagId, null,seller.getId());

        //질문 리스트
        Page<QuestionJPQLResult.SellerViewQuestion> questions = questionService.getQuestionsByTagAndIsAnswered(questionTagId, isAnswered, pageable);
        //새 질문 개수
        Long newQuestions = questionService.getNewQuestionCountOf(questionTagId, null, null);
        //<memberId,질문 횟수> Map
        Map<Long,Long> nthQuestions = questionService.getNthQuestionMap(seller, questions.getContent());
        QuestionResponseDTO.GeneralPage body = QuestionConverter.toGeneralPageDTO(questions, nthQuestions, newQuestions);



        return ApiResponse.onSuccess(body);


    }

    
    //itemId 받게 수정하기
    @PostMapping("user/items/{itemId}/talkbox/{questionCategoryId}")
    @Operation(summary = "질문 작성", description = "질문 작성 API")
    public ApiResponse<QuestionResponseDTO.CreationResult> postQuestion(@RequestBody QuestionRequestDTO.Create request,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @PathVariable("itemId") Long itemId,
                                                                 @PathVariable("questionCategoryId") Long questionCategoryId){

        Member member = memberService.findById(userDetails.getId());
        Item item = itemService.findById(itemId);

        /* 금지 해두면 스웨거로 실험을 못함
        if(member.getRole()==MemberRole.SELLER){
            if(member.getSellerProfile()==item.getSeller()){
                throw new GeneralException(ErrorStatus.FORBIDDEN);
            }
        }
        */

        QuestionCategory category = questionCategoryService.findByCategoryIdAndItemId(questionCategoryId, itemId);

        Question question = questionService.createQuestion(member, item, category,request.getContent());

        //질문 생성시 응답에는 불필요
        //Long nthQuestion = questionService.getTimesMemberAskedSeller(member, item.getSeller());

        QuestionResponseDTO.CreationResult body = QuestionConverter.toCreationResult(question, category.getName());



        return ApiResponse.onSuccess(body);
    }
}
