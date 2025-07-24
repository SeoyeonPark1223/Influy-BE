package com.influy.domain.questionCategory.dto.jpql;

public class CategoryJPQLResult {
    public interface CategoryInfo{
        Long getId();
        String getCategoryName();
        Long getTotalQuestions();
        Long getUncheckedQuestions();
    }
}
