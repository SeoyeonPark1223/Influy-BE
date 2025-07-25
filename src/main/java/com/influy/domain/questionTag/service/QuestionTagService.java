package com.influy.domain.questionTag.service;

import com.influy.domain.question.dto.jpql.QuestionJPQLResult;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionTag.dto.QuestionTagResponseDTO;
import com.influy.domain.questionTag.dto.jpql.TagJPQLResult;
import com.influy.domain.questionTag.entity.QuestionTag;

import java.util.List;

public interface QuestionTagService {
    QuestionTag createTag(String tagName, QuestionCategory questionCategory);

    List<QuestionTagResponseDTO.General> getTagInfoListByCategoryId(Long categoryId, Boolean isAnswered);
}
