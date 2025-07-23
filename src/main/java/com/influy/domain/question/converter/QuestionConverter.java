package com.influy.domain.question.converter;

import com.influy.domain.item.entity.Item;
import com.influy.domain.member.entity.Member;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.dto.jpql.JPQLResult;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionTag.entity.QuestionTag;
import org.springframework.data.domain.Page;

import java.time.ZoneId;
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

    public static QuestionResponseDTO.General toGeneralDTO(JPQLResult.SellerViewQuestion question, Long nthQuestion) {
        return QuestionResponseDTO.General.builder()
                .id(question.getId())
                .memberId(question.getMemberId())
                .content(question.getContent())
                .nickname(question.getNickname())
                .username(question.getUsername())
                .nthQuestion(nthQuestion)
                .createdAt(question.getCreatedAt().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .build();
    }



    public static QuestionResponseDTO.GeneralPage toGeneralPageDTO(Page<JPQLResult.SellerViewQuestion> questions, Map<Long, Long> countMap, Long newQuestions) {
        List<QuestionResponseDTO.General> questionDTOs = questions.getContent().stream().map(question -> toGeneralDTO(question, countMap.get(question.getMemberId()))).toList();

        return QuestionResponseDTO.GeneralPage.builder()
                .questions(questionDTOs)
                .newQuestionCnt(newQuestions)
                .isFirst(questions.isFirst())
                .isLast(questions.isLast())
                .totalPage(questions.getTotalPages())
                .totalElements(questions.getTotalElements())
                .listSize(questions.getSize())
                .build();
    }
}
