package com.influy.domain.category.converter;

import com.influy.domain.category.dto.CategoryResponseDto;
import com.influy.domain.category.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryConverter {
    public static CategoryResponseDto.CategoryDto toCategoryDto(Category category) {
        return CategoryResponseDto.CategoryDto.builder()
                .id(category.getId())
                .name(category.getCategory())
                .build();
    }

    public static CategoryResponseDto.CategoryListDto toItemCategoryListDto(List<Category> categoryList) {
        List<CategoryResponseDto.CategoryDto> categoryDtoList = categoryList.stream()
                .map(CategoryConverter::toCategoryDto).toList();

        return CategoryResponseDto.CategoryListDto.builder()
                .categoryDtoList(categoryDtoList)
                .build();
    }
}
