package com.influy.domain.ai.service;

import com.influy.domain.item.entity.Item;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionTag.entity.QuestionTag;

public interface AiService {
    void generateCategory(Item item);

    QuestionTag classifyQuestion(String content, QuestionCategory questionCategory);
}
