package com.influy.domain.answer.converter;

import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.domain.answer.entity.Answer;

public class AnswerConverter {
    public static AnswerResponseDto.AnswerResultDto toAnswerResultDto(Answer answer) {
        return AnswerResponseDto.AnswerResultDto.builder()
                .build();
    }
}
