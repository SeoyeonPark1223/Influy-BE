package com.influy.domain.faqCategory.converter;

import com.influy.domain.faqCategory.dto.FaqCategoryResponseDTO;
import com.influy.domain.faqCategory.entity.FaqCategory;
import org.springframework.data.domain.Page;

import java.util.List;

public class FaqCategoryConverter {

    public static FaqCategoryResponseDTO.AddResultDto toAddResultDto(List<FaqCategory> faqCategoryList) {
    }

    public static FaqCategoryResponseDTO.PageDto toPageDto(Page<FaqCategory> faqCategoryPage) {
    }

    public static FaqCategoryResponseDTO.ResultDto toResultDto(Long itemId, Long faqCategoryId) {
    }

    public static FaqCategoryResponseDTO.UpdateDto toUpdateDto(List<FaqCategory> faqCategoryList) {
    }
}
