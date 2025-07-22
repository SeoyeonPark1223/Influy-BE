package com.influy.domain.answer.service;

import com.influy.domain.answer.dto.AnswerRequestDto;
import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.domain.answer.entity.Answer;
import com.influy.domain.answer.entity.AnswerType;
import com.influy.domain.item.entity.TalkBoxOpenStatus;
import com.influy.global.jwt.CustomUserDetails;


public interface AnswerService {
    Answer createIndividualAnswer(CustomUserDetails userDetails, Long itemId, Long questionCategoryId, Long questionTagId, Long questionId, AnswerRequestDto.AnswerIndividualDto request, AnswerType answerType);
    AnswerResponseDto.AnswerTagListDto getList(CustomUserDetails userDetails, Long itemId, Long questionCategoryId, Long questionTagId);
    AnswerResponseDto.AnswerCommonResultDto createCommonAnswers(CustomUserDetails userDetails, Long itemId, Long questionCategoryId, AnswerRequestDto.AnswerCommonDto request);
    AnswerResponseDto.DeleteResultDto delete(CustomUserDetails userDetails, Long itemId, Long questionCategoryId, AnswerRequestDto.DeleteDto request);
    AnswerResponseDto.TalkBoxOpenStatusDto changeOpenStatus(CustomUserDetails userDetails, Long itemId, TalkBoxOpenStatus openStatus);
}
