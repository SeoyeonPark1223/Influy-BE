package com.influy.domain.question.converter;

import com.influy.domain.item.entity.Item;
import com.influy.domain.member.entity.Member;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.dto.jpql.QuestionJPQLResult;
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

    public static QuestionResponseDTO.SellerViewQuestion toSellerViewDTO(QuestionJPQLResult.SellerViewQuestion question, Long nthQuestion) {
        return QuestionResponseDTO.SellerViewQuestion.builder()
                .id(question.getId())
                .memberId(question.getMemberId())
                .content(question.getContent())
                .nickname(question.getNickname())
                .username(question.getUsername())
                .tagName(question.getTagName())
                .isNew(!question.getIsChecked())
                .nthQuestion(nthQuestion)
                .createdAt(question.getCreatedAt().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .build();
    }



    public static QuestionResponseDTO.SellerViewPage toSellerViewPageDTO(Page<QuestionJPQLResult.SellerViewQuestion> questions, Map<Long, Long> countMap, Long newQuestions) {
        List<QuestionResponseDTO.SellerViewQuestion> questionDTOs = questions.getContent().stream().map(question -> toSellerViewDTO(question, countMap.get(question.getMemberId()))).toList();

        return QuestionResponseDTO.SellerViewPage.builder()
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
