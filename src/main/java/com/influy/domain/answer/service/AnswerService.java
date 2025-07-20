package com.influy.domain.answer.service;

import com.influy.domain.answer.dto.AnswerRequestDto;
import com.influy.domain.answer.entity.Answer;
import com.influy.domain.answer.entity.AnswerType;
import com.influy.global.jwt.CustomUserDetails;

public interface AnswerService {
    Answer createIndividualAnswer(CustomUserDetails userDetails, Long itemId, Long questionCategoryId, Long questionTagId, Long questionId, AnswerRequestDto.AnswerIndividualDto request, AnswerType answerType);
}
