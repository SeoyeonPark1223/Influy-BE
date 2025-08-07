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
        String getCategoryName();
        Long getWaitingCnt();
        Long getCompletedCnt();
    }

    public interface TopNCategories {
        Long getItemId();
        String getCategoryName();
    }
}
