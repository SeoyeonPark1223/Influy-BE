package com.influy.domain.question.controller;


import com.influy.domain.answer.dto.jpql.AnswerJPQLResult;
import com.influy.domain.answer.service.AnswerService;
import com.influy.domain.answer.dto.AnswerRequestDto;
import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.entity.TalkBoxOpenStatus;
import com.influy.domain.item.service.ItemService;
import com.influy.domain.member.entity.Member;
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
import com.influy.global.apiPayload.code.status.SuccessStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    private final AnswerService answerService;

    @GetMapping("seller/item/talkbox/items/question-categories/{questionTagId}/questions")
    @Operation(summary = "각 태그(소분류)별 전체 질문 조회", description = "답변 완료/대기 요청 따로따로 보내야함(파라미터로)")
    public ApiResponse<QuestionResponseDTO.SellerViewPage> getQuestions(@PathVariable("questionTagId") Long questionTagId,
                                                                        @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @RequestParam(value = "isAnswered",defaultValue = "false") Boolean isAnswered,
                                                                        @ParameterObject PageRequestDto pageable) {

        SellerProfile seller = memberService.checkSeller(userDetails);
        sellerService.checkQuestionOwner(questionTagId, null,seller.getId());


        QuestionResponseDTO.SellerViewPage body = questionService
                .getSellerViewQuestionPage(questionTagId,null, seller,isAnswered, pageable);



        return ApiResponse.onSuccess(body);
    }

    @GetMapping("seller/item/talkbox/{questionCategoryId}/questions")
    @Operation(summary = "각 카테고리 별 전체 질문 조회", description = "태그별 질문 리스트와 동일한데 태그 id 대신 카테고리 id 필요")
    public ApiResponse<QuestionResponseDTO.SellerViewPage> getCategoryQuestions(@PathVariable("questionCategoryId") Long categoryId,
                                                                        @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @RequestParam(value = "isAnswered",defaultValue = "false") Boolean isAnswered,
                                                                        @ParameterObject PageRequestDto pageable) {

        SellerProfile seller = memberService.checkSeller(userDetails);
        sellerService.checkQuestionOwner(null, categoryId,seller.getId());


        QuestionResponseDTO.SellerViewPage body = questionService
                .getSellerViewQuestionPage(null,categoryId, seller,isAnswered, pageable);

        return ApiResponse.onSuccess(body);
    }

    @GetMapping("user/items/{itemId}/talkbox/")
    @Operation(summary = "유저가 톡박스 조회", description = "톡박스 질답 내역 확인")
    public ApiResponse<QuestionResponseDTO.UserViewQNAPage> getUserQnA(@PathVariable("itemId")Long itemId,
                                                                       @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                       @ParameterObject PageRequestDto pageable){

        Member member = memberService.findById(userDetails.getId());

        QuestionResponseDTO.UserViewQNAPage body = questionService.getQNAsOf(member.getId(),itemId,pageable);

        return ApiResponse.onSuccess(body);
    }


    @PostMapping("user/items/{itemId}/talkbox/{questionCategoryId}")
    @Operation(summary = "질문 작성", description = "질문 작성 API")
    public ApiResponse<QuestionResponseDTO.CreationResult> postQuestion(@RequestBody QuestionRequestDTO.Create request,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @PathVariable("itemId") Long itemId,
                                                                 @PathVariable("questionCategoryId") Long questionCategoryId){

        Member member = memberService.findById(userDetails.getId());
        Item item = itemService.findById(itemId);
        if(item.getTalkBoxOpenStatus()!= TalkBoxOpenStatus.OPENED){
            throw new GeneralException(ErrorStatus.TALKBOX_CLOSED);
        }

        /* 금지 해두면 스웨거로 실험을 못함
        if(member.getRole()==MemberRole.SELLER){
            if(member.getSellerProfile()==item.getSeller()){
                throw new GeneralException(ErrorStatus.FORBIDDEN);
            }
        }
        */

        QuestionCategory category = questionCategoryService.findByCategoryIdAndItemId(questionCategoryId, itemId);

        QuestionResponseDTO.CreationResult body = questionService.createQuestion(member, item, category,request.getContent());

        return ApiResponse.onSuccess(body);
    }

    @GetMapping("/seller/items/{itemId}/talkbox/{questionCategoryId}/questions/{questionTagId}/{questionId}")
    @Operation(summary = "셀러뷰 질문별 질문 조회", description = "해당 질문과 해당 질문에 대한 답변 리스트")
    public ApiResponse<QuestionResponseDTO.QnAListDto> viewQnA(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @PathVariable("itemId") Long itemId,
                                                             @PathVariable("questionCategoryId") Long questionCategoryId,
                                                             @PathVariable("questionTagId") Long questionTagId,
                                                             @PathVariable("questionId") Long questionId) {
        return ApiResponse.onSuccess(questionService.viewQnA(userDetails, itemId, questionCategoryId, questionTagId, questionId));
    }

    @DeleteMapping("/seller/items/{itemId}/talkbox/{questionCategoryId}/questions")
    @Operation(summary = "선택한 질문들 삭제 (여러개 가능, 질문 객체 삭제가 아닌 isHidden 처리)")
    public ApiResponse<QuestionResponseDTO.DeleteResultDto> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @PathVariable("itemId") Long itemId,
                                                                 @PathVariable("questionCategoryId") Long questionCategoryId,
                                                                 @RequestBody @Valid QuestionRequestDTO.DeleteDto request) {
        return ApiResponse.onSuccess(questionService.delete(userDetails, itemId, questionCategoryId, request));
    }

}
