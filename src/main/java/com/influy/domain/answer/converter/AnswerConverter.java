package com.influy.domain.answer.converter;

import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.domain.answer.entity.Answer;
import com.influy.domain.answer.entity.AnswerType;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.entity.TalkBoxOpenStatus;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionTag.entity.QuestionTag;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public class AnswerConverter {
    public static AnswerResponseDto.AnswerResultDto toAnswerResultDto(Answer answer) {
        return AnswerResponseDto.AnswerResultDto.builder()
                .questionId(answer.getQuestion().getId())
                .answerType(answer.getAnswerType())
                .answerId(answer.getId())
                .build();
    }

    public static Answer toAnswer(Item item, Question question, String content, AnswerType answerType) {
        return Answer.builder()
                .content(content)
                .item(item)
                .question(question)
                .answerType(answerType)
                .build();
    }

    public static AnswerResponseDto.AnswerTagListDto toAnswerTagListDto(QuestionTag questionTag, List<Answer> answerList) {
        List<String> contentList = answerList.stream().map(Answer::getContent).toList();

        return AnswerResponseDto.AnswerTagListDto.builder()
                .tag(questionTag.getName())
                .answerList(contentList)
                .build();
    }

    public static AnswerResponseDto.AnswerCommonResultDto toAnswerCommonResultDto(Integer size, List<Answer> answerList) {
        List<AnswerResponseDto.AnswerResultDto> answerDtoList = answerList.stream().map(AnswerConverter::toAnswerResultDto).toList();

        return AnswerResponseDto.AnswerCommonResultDto.builder()
                .answeredCnt(size)
                .answerDtoList(answerDtoList)
                .build();
    }


    public static AnswerResponseDto.AnswerViewDto toAnswerViewDto(Answer answer) {
        return AnswerResponseDto.AnswerViewDto.builder()
                .answerId(answer.getId())
                .answerType(answer.getAnswerType())
                .answerContent(answer.getContent())
                .answerTime(answer.getCreatedAt())
                .build();
    }

    public static AnswerResponseDto.AnswerViewListDto toAnswerViewListDto(List<Answer> answerList) {
        List<AnswerResponseDto.AnswerViewDto> answerDtoList = answerList.stream().map(AnswerConverter::toAnswerViewDto).toList();

        return AnswerResponseDto.AnswerViewListDto.builder()
                .answerViewList(answerDtoList).build();
    }
}
