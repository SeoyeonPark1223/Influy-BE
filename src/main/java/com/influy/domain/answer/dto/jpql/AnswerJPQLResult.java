package com.influy.domain.answer.dto.jpql;

import java.time.LocalDateTime;

public class AnswerJPQLResult {
    public interface UserViewQNAInfo {
        String getType();
        Long getId();
        Long getQuestionId();
        String getCategoryName();
        String getAnswerType();
        String getQuestionContent();
        String getContent();
        LocalDateTime getCreatedAt();
    }
}
