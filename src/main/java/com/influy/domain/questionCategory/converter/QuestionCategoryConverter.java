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

    public static QuestionCategoryResponseDto.ViewDto toViewDto(QuestionCategory questionCategory, Integer pending, Integer answered, Integer total) {
        return QuestionCategoryResponseDto.ViewDto.builder()
                .id(questionCategory.getId())
                .category(questionCategory.getCategory())
                .pendingCnt(pending)
                .answeredCnt(answered)
                .totalCnt(total)
                .build();
    }

    public static QuestionCategoryResponseDto.DeleteResultDto toDeleteResultDto(Long id) {
        return QuestionCategoryResponseDto.DeleteResultDto.builder()
                .id(id)
                .build();
    }

    public static QuestionCategoryResponseDto.PageDto toPageDto(Page<QuestionCategory> questionCategoryPage) {
        List<QuestionCategoryResponseDto.ViewDto> questionCategoryList = questionCategoryPage.stream()
                .map(category -> {
                    int pending = category.getQuestionTagList().stream()
                            .flatMap(tag -> tag.getQuestionList().stream())
                            .filter(q -> !q.getIsAnswered())
                            .mapToInt(q -> 1)
                            .sum();

                    int answered = category.getQuestionTagList().stream()
                            .flatMap(tag -> tag.getQuestionList().stream())
                            .filter(q -> q.getIsAnswered())
                            .mapToInt(q -> 1)
                            .sum();

                    return QuestionCategoryConverter.toViewDto(category, pending, answered, pending + answered);
                })
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

//    public static QuestionCategoryResponseDto.General toGeneralDTO(QuestionCategory category,
//                                                                   Integer pending,
//                                                                   Integer answered) {
//        return QuestionCategoryResponseDto.General.builder()
//                .id(category.getId())
//                .name(category.getCategory())
//                .pendingCnt(pending)
//                .answeredCnt(answered)
//                .build();
//    }

    //JPQL result to Preview
    public static QuestionCategoryResponseDto.Preview toPreviewDTO(JPQLQuestionDTO result) {
        return QuestionCategoryResponseDto.Preview.builder()
                .id(result.getCategoryId())
                .name(result.getCategoryName())
                .answeredCnt(0)
                .pendingCnt(0)
                .build();
    }


}
