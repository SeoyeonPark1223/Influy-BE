package com.influy.domain.question.converter;

import com.influy.domain.answer.converter.AnswerConverter;
import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.domain.answer.entity.Answer;
import com.influy.domain.item.entity.Item;
import com.influy.domain.member.entity.Member;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionTag.entity.QuestionTag;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class QuestionConverter {

    public static Question toQuestion(Item item, Member from, String content, QuestionTag questionTag){
        return Question.builder()
                .item(item)
                .member(from)
                .content(content)
                .questionTag(questionTag)
                .build();
    }

    public static QuestionResponseDTO.CreationResult toCreationResult(Question question,String categoryName){

        return QuestionResponseDTO.CreationResult.builder()
                .id(question.getId())
                .content(question.getContent())
                .categoryName(categoryName)
                .createdAt(question.getCreatedAt())
                .build();

    }

    public static QuestionResponseDTO.General toGeneralDTO(Question question, Long nthQuestion) {
        return QuestionResponseDTO.General.builder()
                .id(question.getId())
                .memberId(question.getMember().getId())
                .content(question.getContent())
                .nickname(question.getMember().getNickname())
                .username(question.getMember().getUsername())
                .nthQuestion(nthQuestion)
                .createdAt(question.getCreatedAt())
                .build();
    }



    public static QuestionResponseDTO.GeneralPage toGeneralPageDTO(Page<Question> questions, Map<Long, Long> countMap) {


        return QuestionResponseDTO.GeneralPage.builder()
                .isFirst(questions.isFirst())
                .isLast(questions.isLast())
                .totalPage(questions.getTotalPages())
                .totalElements(questions.getTotalElements())
                .listSize(questions.getSize())
                .questions(questions.map(question->QuestionConverter.toGeneralDTO(question, countMap.get(question.getMember().getId()))).getContent())
                .build();
    }

    public static QuestionResponseDTO.QnAListDto toQnAListDto(Question question, List<Answer> answerList, Long nth) {
        QuestionResponseDTO.QuestionViewDto questionDto = QuestionConverter.toQuestionViewDto(question, nth);
        AnswerResponseDto.AnswerViewListDto answerListDto = AnswerConverter.toAnswerViewListDto(answerList);

        return QuestionResponseDTO.QnAListDto.builder()
                .questionDto(questionDto)
                .answerListDto(answerListDto)
                .build();
    }

    private static QuestionResponseDTO.QuestionViewDto toQuestionViewDto(Question question, Long nth) {
        Member member = question.getMember();
        return QuestionResponseDTO.QuestionViewDto.builder()
                .questionId(question.getId())
                .memberId(member.getId())
                .username(member.getUsername())
                .content(question.getContent())
                .nthQuestion(nth)
                .questionTime(question.getCreatedAt())
                .questionTag(question.getQuestionTag().getName())
                .build();
    }

    public static QuestionResponseDTO.DeleteResultDto toDeleteResultDto(List<Long> questionList) {
        return QuestionResponseDTO.DeleteResultDto.builder()
                .questionIdList(questionList)
                .build();
    }
}