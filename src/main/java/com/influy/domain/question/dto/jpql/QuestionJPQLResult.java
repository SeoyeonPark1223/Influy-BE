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
        String getProfileImg();
        String getNickname();
        String getUsername();
        Boolean getIsChecked();
        String getContent();
        String getTagName();
        Long getTagId();
        Date getCreatedAt();
    }
}
