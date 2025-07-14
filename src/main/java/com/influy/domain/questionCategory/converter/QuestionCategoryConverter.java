package com.influy.domain.questionCategory.converter;

import com.influy.domain.item.entity.Item;
import com.influy.domain.question.dto.JPQLQuestionDTO;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDto;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import org.springframework.data.domain.Page;

import java.util.List;

public class QuestionCategoryConverter {

    public static QuestionCategory toQuestionCategory(Item item, String category) {
        return QuestionCategory.builder()
                .category(category)
                .item(item)
                .build();
    }

    public static QuestionCategoryResponseDto.ViewDto toViewDto(QuestionCategory questionCategory) {
        return QuestionCategoryResponseDto.ViewDto.builder()
                .id(questionCategory.getId())
                .category(questionCategory.getCategory())
                .build();
    }

    public static QuestionCategoryResponseDto.DeleteResultDto toDeleteResultDto(Long id) {
        return QuestionCategoryResponseDto.DeleteResultDto.builder()
                .id(id)
                .build();
    }

    public static QuestionCategoryResponseDto.PageDto toPageDto(Page<QuestionCategory> questionCategoryPage) {
        List<QuestionCategoryResponseDto.ViewDto> questionCategoryList = questionCategoryPage.stream()
                .map(QuestionCategoryConverter::toViewDto)
                .toList();

        return QuestionCategoryResponseDto.PageDto.builder()
                .viewList(questionCategoryList)
                .listSize(questionCategoryPage.getContent().size())
                .totalPage(questionCategoryPage.getTotalPages())
                .totalElements(questionCategoryPage.getTotalElements())
                .isFirst(questionCategoryPage.isFirst())
                .isLast(questionCategoryPage.isLast())
                .build();
    }

    //JPQL result to Preview
    public static QuestionCategoryResponseDto.Preview toPreviewDTO(JPQLQuestionDTO result) {
        return QuestionCategoryResponseDto.Preview.builder()
                .id(result.getCategoryId())
                .name(result.getCategoryName())
                .answeredCnt(0)
                .pendingCnt(0)
                .build();
    }

    public static QuestionCategoryResponseDto.GenerateResultDto toGenerateResultDto(List<QuestionCategory> questionCategoryList) {
        List<QuestionCategoryResponseDto.ViewDto> viewList = questionCategoryList.stream()
                .map(QuestionCategoryConverter::toViewDto)
                .toList();

        return QuestionCategoryResponseDto.GenerateResultDto.builder()
                .viewList(viewList)
                .build();
    }
}
