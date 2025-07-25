package com.influy.domain.questionCategory.converter;

import com.influy.domain.item.entity.Item;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDto;
import com.influy.domain.questionCategory.dto.jpql.CategoryJPQLResult;
import com.influy.domain.questionCategory.entity.QuestionCategory;

import java.util.List;

public class QuestionCategoryConverter {

    public static QuestionCategory toQuestionCategory(Item item, String name) {
        return QuestionCategory.builder()
                .name(name)
                .item(item)
                .build();
    }

    public static QuestionCategoryResponseDto.ViewDto toViewDto(QuestionCategory questionCategory) {
        return QuestionCategoryResponseDto.ViewDto.builder()
                .questionCategoryId(questionCategory.getId())
                .questionCategoryName(questionCategory.getName())
                .build();
    }

    public static QuestionCategoryResponseDto.TalkBoxCategoryInfoDTO toTalkBoxCategoryInfoDTO(CategoryJPQLResult.CategoryInfo categoryInfo) {
        return QuestionCategoryResponseDto.TalkBoxCategoryInfoDTO.builder()
                .questionCategoryId(categoryInfo.getId())
                .questionCategoryName(categoryInfo.getCategoryName())
                .questionCnt(categoryInfo.getTotalQuestions())
                .unCheckedCnt(categoryInfo.getUncheckedQuestions())
                .build();
    }


    public static QuestionCategoryResponseDto.ViewListDto toViewListDto(List<QuestionCategory> questionCategoryList) {
        List<QuestionCategoryResponseDto.ViewDto> viewList = questionCategoryList.stream()
                .map(QuestionCategoryConverter::toViewDto)
                .toList();

        return QuestionCategoryResponseDto.ViewListDto.builder()
                .viewList(viewList)
                .build();
    }

    public static QuestionCategoryResponseDto.GeneratedResultDto toGeneratedResultDto(List<String> generatedCategoryList) {
        return QuestionCategoryResponseDto.GeneratedResultDto.builder()
                .generatedNameList(generatedCategoryList)
                .build();
    }

    public static QuestionCategoryResponseDto.TalkBoxCategoryInfoListDTO toTalkBoxCategoryInfoListDTO(List<CategoryJPQLResult.CategoryInfo> result, Long waitingCnt, Long completedCnt) {
        List<QuestionCategoryResponseDto.TalkBoxCategoryInfoDTO> content = result.stream().map(QuestionCategoryConverter::toTalkBoxCategoryInfoDTO).toList();

        return QuestionCategoryResponseDto.TalkBoxCategoryInfoListDTO.builder()
                .categoryList(content)
                .waitingCnt(waitingCnt)
                .completedCnt(completedCnt)
                .build();
    }
}
