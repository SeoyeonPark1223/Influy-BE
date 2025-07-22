package com.influy.domain.question.converter;

import com.influy.domain.item.entity.Item;
import com.influy.domain.member.entity.Member;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionTag.entity.QuestionTag;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
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
}
