package com.influy.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JPQLQuestionDTO {
    private Long categoryId;
    private String categoryName;
    private Boolean isAnswered;
    private Long count;

    public String toString(){
        return this.categoryId.toString() + " " + this.categoryName + " " + this.isAnswered + " " + this.count;
    }

}
