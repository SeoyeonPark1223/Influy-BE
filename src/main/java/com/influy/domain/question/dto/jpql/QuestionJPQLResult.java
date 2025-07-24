package com.influy.domain.question.dto.jpql;

import java.util.Date;

public class QuestionJPQLResult {
    public interface MemberQuestionCount{
        Long getMemberId();
        Long getCnt();
    }

    public interface SellerViewQuestion {
        Long getId();
        Long getMemberId();
        String getNickname();
        String getUsername();
        String getContent();
        Date getCreatedAt();
    }
}
