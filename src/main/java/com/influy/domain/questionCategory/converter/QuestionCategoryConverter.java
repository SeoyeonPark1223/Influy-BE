package com.influy.domain.questionCategory.converter;

import com.influy.domain.item.entity.Item;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDto;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

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

    public static QuestionCategoryResponseDto.DeleteResultDto toDeleteResultDto(Long id) {
        return QuestionCategoryResponseDto.DeleteResultDto.builder()
                .id(id)
                .build();
    }

    public static QuestionCategoryResponseDto.ViewWithCntDto toViewWithCntDto(QuestionCategory questionCategory, Integer questionCnt, Integer unCheckedCnt) {
        return QuestionCategoryResponseDto.ViewWithCntDto.builder()
                .questionCategoryId(questionCategory.getId())
                .questionCategoryName(questionCategory.getName())
                .questionCnt(questionCnt)
                .unCheckedCnt(unCheckedCnt)
                .build();
    }

    public static QuestionCategoryResponseDto.ListDto toListDto(List<QuestionCategory> questionCategoryList, Map<Long, Integer> questionCntMap, Map<Long, Integer> unCheckedCntMap) {
        List<QuestionCategoryResponseDto.ViewWithCntDto> qcList = questionCategoryList.stream()
                .map(qc -> toViewWithCntDto(
                        qc,
                        questionCntMap.getOrDefault(qc.getId(), 0),
                        unCheckedCntMap.getOrDefault(qc.getId(), 0)
                ))
                .toList();

        return QuestionCategoryResponseDto.ListDto.builder()
                .viewList(qcList)
                .listSize(qcList.size())
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
