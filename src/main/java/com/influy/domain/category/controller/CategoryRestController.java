package com.influy.domain.category.controller;

import com.influy.domain.category.converter.CategoryConverter;
import com.influy.domain.category.dto.CategoryResponseDto;
import com.influy.domain.category.entity.Category;
import com.influy.domain.category.repository.CategoryRepository;
import com.influy.domain.category.service.CategoryService;
import com.influy.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "아이템 카테고리", description = "아이템 카테고리 조회 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryRestController {
    private final CategoryService categoryService;

    @GetMapping()
    @Operation(summary = "아이템 카테고리 리스트 조회")
    public ApiResponse<CategoryResponseDto.CategoryListDto> getCategories() {
        List<Category> categoryList = categoryService.getCategories();
        return ApiResponse.onSuccess(CategoryConverter.toItemCategoryListDto(categoryList));
    }
}
