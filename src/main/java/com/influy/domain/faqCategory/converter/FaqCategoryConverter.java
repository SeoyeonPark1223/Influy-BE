package com.influy.domain.faqCategory.converter;

import com.influy.domain.faqCategory.dto.FaqCategoryRequestDto;
import com.influy.domain.faqCategory.dto.FaqCategoryResponseDto;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.item.entity.Item;
import org.springframework.data.domain.Page;

import java.util.List;

public class FaqCategoryConverter {

    public static FaqCategoryResponseDto.AddResultDto toAddResultDto(List<FaqCategory> faqCategoryList) {
        List<Long> idList = faqCategoryList.stream()
                .map(FaqCategory::getId).toList();

        return FaqCategoryResponseDto.AddResultDto.builder()
                .idList(idList)
                .build();
    }

    public static FaqCategoryResponseDto.ViewDto toViewDto(FaqCategory faqCategory) {
        return FaqCategoryResponseDto.ViewDto.builder()
                .id(faqCategory.getId())
                .category(faqCategory.getCategory())
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

    public static FaqCategoryResponseDto.ResultDto toResultDto(List<FaqCategoryRequestDto.DeleteDto> faqCategoryIdList) {
        List<Long> idList = faqCategoryIdList.stream()
                .map(FaqCategoryRequestDto.DeleteDto::getId)
                .toList();

        return FaqCategoryResponseDto.ResultDto.builder()
                .idList(idList)
                .build();
    }

    public static FaqCategory toFaqCategory(FaqCategoryRequestDto.AddDto request, Item item) {
        return FaqCategory.builder()
                .item(item)
                .category(request.getCategory())
                .build();
    }

    public static FaqCategoryResponseDto.UpdateResultDto toUpdateResultDto(List<FaqCategory> faqCategoryList) {
        List<FaqCategoryResponseDto.ViewDto> updatedList = faqCategoryList.stream()
                .map(f -> FaqCategoryResponseDto.ViewDto.builder()
                        .id(f.getId())
                        .category(f.getCategory())
                        .build())
                .toList();

        return FaqCategoryResponseDto.UpdateResultDto.builder()
                .updatedList(updatedList)
                .build();
    }
}
