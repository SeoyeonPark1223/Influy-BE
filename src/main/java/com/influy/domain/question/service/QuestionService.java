package com.influy.domain.question.service;

import com.influy.domain.item.entity.Item;
import com.influy.domain.member.entity.Member;
import com.influy.domain.question.dto.jpql.QuestionJPQLResult;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface QuestionService {

    Page<QuestionJPQLResult.SellerViewQuestion> getQuestionsByTagOrCategoryAndIsAnswered(Long questionTagId, Long questionCategoryId, Boolean isAnswered, Pageable pageable);

    Question createQuestion(Member member, Item item, QuestionCategory questionCategory, String content);

    Map<Long, Long> getNthQuestionMap(SellerProfile seller, List<QuestionJPQLResult.SellerViewQuestion> questions);

    Long getNewQuestionCountOf(Long questionTagId, Long questionCategoryId, Long itemId);

    void setAllChecked(List<Long> questionIds);
}
