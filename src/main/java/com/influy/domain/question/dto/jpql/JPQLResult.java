package com.influy.domain.question.dto.jpql;

import lombok.Getter;

public class JPQLResult {
    @Getter
    public static class MemberQuestionCount{
        private Long memberId;
        private Long cnt;
    }
}
