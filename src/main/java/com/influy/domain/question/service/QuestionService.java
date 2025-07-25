package com.influy.domain.question.service;

import com.influy.domain.item.entity.Item;
import com.influy.domain.member.entity.Member;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface QuestionService {

    Page<Question> getQuestionsByTag(Long questionTagId, SellerProfile sellerProfile, Boolean isAnswered, Pageable pageable);

    Question createQuestion(Member member, Item item, QuestionCategory questionCategory, String content);

    Long getTimesMemberAskedSeller(Member member, SellerProfile seller);

    Map<Long, Long> getNthQuestionMap(SellerProfile seller, Page<Question> questions);
}
