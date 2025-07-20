package com.influy.domain.answer.converter;

import com.influy.domain.answer.dto.AnswerRequestDto;
import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.domain.answer.entity.Answer;
import com.influy.domain.answer.entity.AnswerType;
import com.influy.domain.question.entity.Question;
import com.influy.domain.sellerProfile.entity.SellerProfile;

public class AnswerConverter {
    public static AnswerResponseDto.AnswerResultDto toAnswerResultDto(Answer answer) {
        return AnswerResponseDto.AnswerResultDto.builder()
                .questionId(answer.getQuestion().getId())
                .answerType(answer.getAnswerType())
                .answerId(answer.getId())
                .build();
    }

    public static Answer toAnswer(SellerProfile seller, Question question, AnswerRequestDto.AnswerIndividualDto request, AnswerType answerType) {
        return Answer.builder()
                .content(request.getAnswerContent())
                .seller(seller)
                .question(question)
                .answerType(answerType)
                .build();
    }
}
