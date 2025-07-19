package com.influy.domain.question.service;

import com.influy.domain.member.entity.Member;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {

    Page<Question> getQuestionList(QuestionCategory category, Pageable p);

    Page<Question> getQuestionsByCategory(Long questionCategoryId, Boolean isAnswered, Pageable pageable);

    Question createQuestion(Member member, SellerProfile seller, Long questionCategoryId, String content);
}
