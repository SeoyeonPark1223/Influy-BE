package com.influy.domain.questionTag.dto.jpql;

public class TagJPQLResult {
    public interface QuestionTagInfo {
        Long getId();
        String getTagName();
        Long getTotalQuestions();
        Long getUncheckedQuestions();
    }
}
