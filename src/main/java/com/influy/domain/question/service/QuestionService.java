package com.influy.domain.question.service;

import com.influy.domain.answer.dto.jpql.AnswerJPQLResult;
import com.influy.domain.answer.dto.AnswerRequestDto;
import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.domain.item.entity.Item;
import com.influy.domain.member.entity.Member;
import com.influy.domain.question.dto.jpql.QuestionJPQLResult;
import com.influy.domain.question.dto.QuestionRequestDTO;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.common.PageRequestDto;
import com.influy.global.apiPayload.code.status.SuccessStatus;
import com.influy.global.jwt.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface QuestionService {



    QuestionResponseDTO.CreationResult createQuestion(Member member, Item item, QuestionCategory questionCategory, String content);

    QuestionResponseDTO.UserViewQNAPage getQNAsOf(Long memberId, Long itemId, PageRequestDto pageable);

    QuestionResponseDTO.QnAListDto viewQnA(CustomUserDetails userDetails, Long itemId, Long questionCategoryId, Long questionTagId, Long questionId);

    QuestionResponseDTO.DeleteResultDto delete(CustomUserDetails userDetails, Long itemId, Long questionCategoryId, QuestionRequestDTO.DeleteDto request);

    QuestionResponseDTO.SellerViewPage getSellerViewQuestionPage(Long questionTagId, Long questionCategoryId, SellerProfile seller, Boolean isAnswered, PageRequestDto pageable);
}
