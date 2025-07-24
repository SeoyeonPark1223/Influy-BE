package com.influy.domain.question.service;

import com.influy.domain.ai.service.AiService;
import com.influy.domain.answer.dto.jpql.AnswerJPQLResult;
import com.influy.domain.answer.repository.AnswerRepository;
import com.influy.domain.answer.converter.AnswerConverter;
import com.influy.domain.answer.dto.AnswerRequestDto;
import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.domain.answer.entity.Answer;
import com.influy.domain.item.entity.Item;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.service.MemberService;
import com.influy.domain.question.converter.QuestionConverter;
import com.influy.domain.question.dto.jpql.QuestionJPQLResult;
import com.influy.domain.question.dto.QuestionRequestDTO;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.entity.Question;
import com.influy.domain.question.repository.QuestionRepository;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.repository.QuestionCategoryRepository;
import com.influy.domain.questionTag.entity.QuestionTag;
import com.influy.domain.questionTag.repository.QuestionTagRepository;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.common.PageRequestDto;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.jwt.CustomUserDetails;
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
    private final AnswerRepository answerRepository;
    private final MemberService memberService;


    @Override
    public Page<QuestionJPQLResult.SellerViewQuestion> getQuestionsByTagOrCategoryAndIsAnswered(Long questionTagId, Long questionCategoryId, Boolean isAnswered, Pageable pageable) {
        if(questionTagId!=null){
            return questionRepository.findAllByQuestionTagIdAndIsAnswered(questionTagId,isAnswered,pageable);
        }else{
            return questionRepository.findAllByQuestionCategoryAndIsAnswered(questionCategoryId, isAnswered, pageable);
        }
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


    //멤버 id별 seller에 대한 질문 횟수 구하는 메서드
    @Override
    public Map<Long, Long> getNthQuestionMap(SellerProfile seller, List<QuestionJPQLResult.SellerViewQuestion> questions) {
        List<Long> memberIds = questions.stream()
                .map(QuestionJPQLResult.SellerViewQuestion::getMemberId)
                .distinct()
                .collect(Collectors.toList());

        List<QuestionJPQLResult.MemberQuestionCount> counts = questionRepository.countQuestionsBySellerAndMemberIds(seller, memberIds);

        return counts.stream()
                .collect(Collectors.toMap(QuestionJPQLResult.MemberQuestionCount::getMemberId, QuestionJPQLResult.MemberQuestionCount::getCnt));
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

    @Override
    @Transactional
    public void setAllChecked(List<Long> questionIds) {
        questionRepository.setQuestionsAsChecked(questionIds);
    }

    @Override
    public Page<AnswerJPQLResult.UserViewQNAInfo> getQNAsOf(Long memberId, Long itemId, PageRequestDto pageableDto) {

        Pageable pageable = pageableDto.toPageable();
        //유저에게는 question 의 Hidden 상태와 무관하게 모두 보여줌
        return questionRepository.findAllByMemberIdAndItemId(memberId, itemId, pageable);
    }

    @Override
    public QuestionResponseDTO.QnAListDto viewQnA(CustomUserDetails userDetails, Long itemId, Long questionCategoryId, Long questionTagId, Long questionId) {
        SellerProfile seller = memberService.checkSeller(userDetails);
        Question question = questionRepository.findValidQuestion(itemId, questionCategoryId, questionTagId, questionId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_INVALID_RELATION));
        Long nth = questionRepository.countByMemberAndSeller(userDetails.getMember(), seller);
        List<Answer> answerList = question.getAnswerList();

        return QuestionConverter.toQnAListDto(question, answerList, nth);
    }

    @Override
    @Transactional
    public QuestionResponseDTO.DeleteResultDto delete(CustomUserDetails userDetails, Long itemId, Long questionCategoryId, QuestionRequestDTO.DeleteDto request) {
        memberService.checkSeller(userDetails);

        // 질문을 삭제하면 해당 질문의 모든 답변들도 삭제된다고 가정 (셀러 입장)
        List<Question> questionList= questionRepository.findAllById(request.getQuestionIdList());
        for (Question question : questionList) {
            question.setIsHidden(true);
        }

        return QuestionConverter.toDeleteResultDto(request.getQuestionIdList());
    }
}
