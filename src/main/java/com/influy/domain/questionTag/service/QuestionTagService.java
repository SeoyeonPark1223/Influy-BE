package com.influy.domain.questionTag.service;

import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionTag.entity.QuestionTag;

public interface QuestionTagService {
    QuestionTag createTag(String tagName, QuestionCategory questionCategory);
}
