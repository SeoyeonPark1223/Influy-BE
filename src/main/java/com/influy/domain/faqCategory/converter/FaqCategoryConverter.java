package com.influy.domain.faqCategory.converter;

import com.influy.domain.faqCategory.dto.FaqCategoryRequestDto;
import com.influy.domain.faqCategory.dto.FaqCategoryResponseDto;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.item.entity.Item;
import org.springframework.data.domain.Page;

import java.util.List;

public class FaqCategoryConverter {

    public static FaqCategoryResponseDto.ViewDto toViewDto(FaqCategory faqCategory) {
        return FaqCategoryResponseDto.ViewDto.builder()
                .id(faqCategory.getId())
                .category(faqCategory.getCategory())
                .categoryOrder(faqCategory.getCategoryOrder())
                .build();
    }

    public static FaqCategoryResponseDto.PageDto toPageDto(Page<FaqCategory> faqCategoryPage) {
        List<FaqCategoryResponseDto.ViewDto> faqCategoryList = faqCategoryPage.stream()
                .map(FaqCategoryConverter::toViewDto)
                .toList();

        return FaqCategoryResponseDto.PageDto.builder()
                .viewList(faqCategoryList)
                .listSize(faqCategoryPage.getContent().size())
                .totalPage(faqCategoryPage.getTotalPages())
                .totalElements(faqCategoryPage.getTotalElements())
                .isFirst(faqCategoryPage.isFirst())
                .isLast(faqCategoryPage.isLast())
                .build();
    }

    public static FaqCategoryResponseDto.DeleteResultDto toDeleteResultDto(FaqCategoryRequestDto.DeleteDto request) {
        return FaqCategoryResponseDto.DeleteResultDto.builder()
                .id(request.getId())
                .build();
    }

    public static FaqCategory toFaqCategory(FaqCategoryRequestDto.AddDto request, Item item, Integer nextNum) {
        return FaqCategory.builder()
                .item(item)
                .category(request.getCategory())
                .categoryOrder(nextNum)
                .build();
    }

    public static FaqCategoryResponseDto.UpdateOrderResultDto toUpdateOrderResultDto(List<FaqCategory> faqCategoryList) {
        List<FaqCategoryResponseDto.ViewDto> updatedList = faqCategoryList.stream()
                .map(f -> FaqCategoryResponseDto.ViewDto.builder()
                        .id(f.getId())
                        .category(f.getCategory())
                        .categoryOrder(f.getCategoryOrder())
                        .build())
                .toList();

        return FaqCategoryResponseDto.UpdateOrderResultDto.builder()
                .updatedList(updatedList)
                .build();
    }
}
