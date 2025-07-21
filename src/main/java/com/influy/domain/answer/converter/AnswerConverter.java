package com.influy.domain.answer.converter;

import com.influy.domain.answer.dto.AnswerRequestDto;
import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.domain.answer.entity.Answer;
import com.influy.domain.answer.entity.AnswerType;
import com.influy.domain.item.entity.TalkBoxOpenStatus;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionTag.entity.QuestionTag;
import com.influy.domain.sellerProfile.entity.SellerProfile;

import java.util.List;

public class AnswerConverter {
    public static AnswerResponseDto.AnswerResultDto toAnswerResultDto(Answer answer) {
        return AnswerResponseDto.AnswerResultDto.builder()
                .questionId(answer.getQuestion().getId())
                .answerType(answer.getAnswerType())
                .answerId(answer.getId())
                .build();
    }

    public static Answer toAnswer(SellerProfile seller, Question question, String content, AnswerType answerType) {
        return Answer.builder()
                .content(content)
                .seller(seller)
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

    public static AnswerResponseDto.DeleteResultDto toDeleteResultDto(List<Long> questionList) {
        return AnswerResponseDto.DeleteResultDto.builder()
                .questionIdList(questionList)
                .build();
    }

    public static AnswerResponseDto.TalkBoxOpenStatusDto toTalkBoxOpenStatusDto(Long itemId, TalkBoxOpenStatus openStatus) {
        return AnswerResponseDto.TalkBoxOpenStatusDto.builder()
                .itemId(itemId)
                .status(openStatus)
                .build();
    }
}
