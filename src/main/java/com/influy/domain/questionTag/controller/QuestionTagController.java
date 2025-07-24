package com.influy.domain.questionTag.controller;

import com.influy.domain.member.entity.Member;
import com.influy.domain.member.service.MemberService;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.dto.jpql.QuestionJPQLResult;
import com.influy.domain.questionCategory.service.QuestionCategoryService;
import com.influy.domain.questionTag.converter.QuestionTagConverter;
import com.influy.domain.questionTag.dto.QuestionTagResponseDTO;
import com.influy.domain.questionTag.dto.jpql.TagJPQLResult;
import com.influy.domain.questionTag.service.QuestionTagService;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.service.SellerProfileService;
import com.influy.domain.sellerProfile.service.SellerProfileServiceImpl;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "질문 관리")
public class QuestionTagController {

    private final MemberService memberService;
    private final QuestionTagService questionTagService;
    private final SellerProfileService sellerProfileService;
    private final QuestionCategoryService questionCategoryService;

    @GetMapping("seller/item/talkbox/{questionCategoryId}/tags")
    @Operation(summary = "각 카테고리 별 소분류 리스트", description = "카테고리 전체는...")
    public ApiResponse<List<QuestionTagResponseDTO.General>> getTagListOfCategory(@PathVariable("questionCategoryId") Long categoryId,
                                                    @RequestParam(name = "isAnswered", defaultValue = "false") Boolean isAnswered,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        SellerProfile seller = memberService.checkSeller(userDetails);
        sellerProfileService.checkQuestionOwner(null,categoryId,seller.getId());

        //태그 정보 리스트 조회+전체 버튼까지
        List<QuestionTagResponseDTO.General> body = questionTagService.getTagInfoListByCategoryId(categoryId, isAnswered);



        return ApiResponse.onSuccess(body);
    }


}
