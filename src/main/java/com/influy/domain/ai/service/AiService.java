package com.influy.domain.ai.service;

import com.influy.domain.item.entity.Item;
import com.influy.domain.questionCategory.entity.QuestionCategory;

public interface AiService {
    void generateCategory(Item item);

    String classifyQuestion(String content, QuestionCategory questionCategory);
}
