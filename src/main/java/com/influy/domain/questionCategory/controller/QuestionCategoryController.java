package com.influy.domain.questionCategory.controller;

import com.influy.domain.question.converter.QuestionConverter;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.entity.Question;
import com.influy.domain.question.service.QuestionService;
import com.influy.domain.questionCategory.converter.QuestionCategoryConverter;
import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDTO;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDTO;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.service.QuestionCategoryService;
import com.influy.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("seller/items/{itemId}/questions/categories/{sellerId}")//로그인 구현 시 sellerID 삭제
@RequiredArgsConstructor
@Tag(name ="질문 카테고리")
public class QuestionCategoryController {

    private final QuestionCategoryService questionCategoryService;
    private final QuestionService questionService;

    //질문 카테고리 추가
    @PostMapping
    @Operation(summary = "질문 카테고리 추가",description = "특정 아이템에 질문 카테고리를 추가합니다")
    public ApiResponse<QuestionCategoryResponseDTO.General> createCategory(@PathVariable("itemId") Long itemId,
                                                        @PathVariable("sellerId") Long sellerId,
                                                        @RequestBody QuestionCategoryRequestDTO.Create request) {

        QuestionCategory category = questionCategoryService.createCategory(sellerId,itemId,request);
        QuestionCategoryResponseDTO.General body = QuestionCategoryConverter.toGeneralDTO(category,0,0);

        return ApiResponse.onSuccess(body);
    }

    //질문 카테고리 리스트+질문 2개 조회
    @GetMapping
    @Operation(summary = "질문 카테고리 리스트+질문2개",description = "질문 카테고리 리스트와 함께 해당 카테고리의 최신 질문 2개를 제공")
    public ApiResponse<List<QuestionCategoryResponseDTO.Preview>> getCategoriesAnd2Question(@PathVariable("itemId") Long itemId,
                                                                                            @PathVariable("sellerId") Long sellerId,
                                                                   @ParameterObject Pageable pageable) {

        Page<QuestionCategory> categories = questionCategoryService.getCategoryList(sellerId,itemId,pageable);

        List<QuestionCategoryResponseDTO.Preview> body = questionCategoryService.getPreviewDTO(categories,itemId);

        return ApiResponse.onSuccess(body);
    }

    //질문 카테고리 수정
    @PutMapping
    @Operation(summary = "질문 카테고리 수정",description = "질문 카테고리를 수정합니다. 수정할거 이름밖에 없지않나")
    public ApiResponse<QuestionCategory> updateCategory(@PathVariable("itemId") Long itemId,
                                                        @PathVariable("sellerId") Long sellerId) {
        return null;
    }

    //질문 카테고리 삭제
    @DeleteMapping
    @Operation(summary = "질문 카테고리 삭제",description = "질문 카테고리를 삭제합니다. 해당 질문 카테고리에 속한 질문들은 기타 카테고리로 옮겨집니다.")
    public ApiResponse<QuestionCategory> deleteCategory(@PathVariable("itemId") Long itemId,
                                                        @PathVariable("sellerId") Long sellerId) {
        return null;
    }

}
