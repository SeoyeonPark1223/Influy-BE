package com.influy.domain.questionCategory.dto.jpql;

public class CategoryJPQLResult {
    public interface CategoryInfo{
        Long getId();
        Long getIsAnswered();
        String getCategoryName();
        Long getTotalQuestions();
        Long getUncheckedQuestions();
    }

    public interface IsAnswered{
        Boolean getIsAnswered();
        Long getTotalQuestions();
    }
}
