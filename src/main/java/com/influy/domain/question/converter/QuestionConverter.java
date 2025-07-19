package com.influy.domain.question.converter;

import com.influy.domain.member.entity.Member;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionTag.entity.QuestionTag;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;

public class QuestionConverter {

    public static Question toQuestion(SellerProfile to, Member from, String content, QuestionTag questionTag){
        return Question.builder()
                .seller(to)
                .member(from)
                .content(content)
                .questionTag(questionTag)
                .build();
    }

    public static QuestionResponseDTO.General toGeneralDTO(Question question) {
        return QuestionResponseDTO.General.builder()
                .id(question.getId())
                .content(question.getContent())
                .nickname(question.getMember().getNickname())
                .itemPeriod(question.getItemPeriod())
                .createdAt(question.getCreatedAt())
                .build();
    }

    //native sql 전용 converter
    public static QuestionResponseDTO.General toGeneralDTO(Object[] row, String nickname) {
        return QuestionResponseDTO.General.builder()
                .id(((Number) row[0]).longValue())
                .nickname(nickname)
                .content((String) row[2])
                .createdAt(((Timestamp) row[3]).toLocalDateTime())
                .itemPeriod((Integer) row[4])
                .build();
    }

    public static QuestionResponseDTO.GeneralPage toGeneralPageDTO(Page<Question> questions) {


        return QuestionResponseDTO.GeneralPage.builder()
                .isFirst(questions.isFirst())
                .isLast(questions.isLast())
                .totalPage(questions.getTotalPages())
                .totalElements(questions.getTotalElements())
                .listSize(questions.getSize())
                .questions(questions.map(QuestionConverter::toGeneralDTO).getContent())
                .build();
    }
}
