package com.influy.domain.question.service;

import com.influy.domain.ai.service.AiService;
import com.influy.domain.item.entity.Item;
import com.influy.domain.member.entity.Member;
import com.influy.domain.question.converter.QuestionConverter;
import com.influy.domain.question.dto.jpql.JPQLResult;
import com.influy.domain.question.entity.Question;
import com.influy.domain.question.repository.QuestionRepository;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.repository.QuestionCategoryRepository;
import com.influy.domain.questionTag.entity.QuestionTag;
import com.influy.domain.questionTag.repository.QuestionTagRepository;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final QuestionTagRepository questionTagRepository;
    private final AiService aiService;


    @Override
    public Page<JPQLResult.SellerViewQuestion> getQuestionsByTagAndIsAnswered(Long questionTagId, Boolean isAnswered, Pageable pageable) {


        return questionRepository.findAllByQuestionTagIdAndIsAnswered(questionTagId,isAnswered,pageable);
    }

    @Override
    @Transactional
    public Question createQuestion(Member member, Item item, QuestionCategory questionCategory, String content) {

        QuestionTag questionTag = aiService.classifyQuestion(content, questionCategory);
        Question question = QuestionConverter.toQuestion(item,member,content, questionTag);
        questionTag.getQuestionList().add(question);
        item.getQuestionList().add(question);


        return questionRepository.save(question);
    }

    @Override
    public Long getTimesMemberAskedSeller(Member member, SellerProfile seller) {
        return questionRepository.countByMemberAndSeller(member,seller);
    }

    //멤버 id별 seller에 대한 질문 횟수 구하는 메서드
    @Override
    public Map<Long, Long> getNthQuestionMap(SellerProfile seller, List<JPQLResult.SellerViewQuestion> questions) {
        List<Long> memberIds = questions.stream()
                .map(q -> q.getMemberId())
                .distinct()
                .collect(Collectors.toList());

        List<JPQLResult.MemberQuestionCount> counts = questionRepository.countQuestionsBySellerAndMemberIds(seller, memberIds);

        return counts.stream()
                .collect(Collectors.toMap(JPQLResult.MemberQuestionCount::getMemberId, JPQLResult.MemberQuestionCount::getCnt));
    }

    //isChecked==false인 질문 구하기
    @Override
    public Long getNewQuestionCountOf(Long questionTagId, Long questionCategoryId, Long itemId) {

        if(questionTagId!=null){
            return questionRepository.countByQuestionTagIdAndIsCheckedFalse(questionTagId);
        } else if (questionCategoryId!=null) {
            return questionRepository.countByQuestionCategoryIdAndIsCheckedFalse(questionCategoryId);
        }else if (itemId!=null) {
            return questionRepository.countByItemIdAndIsCheckedFalse(itemId);
        }else{
            return 0L;
        }
    }


}
