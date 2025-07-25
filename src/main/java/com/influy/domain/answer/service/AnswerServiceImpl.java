package com.influy.domain.answer.service;

import com.influy.domain.answer.converter.AnswerConverter;
import com.influy.domain.answer.dto.AnswerRequestDto;
import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.domain.answer.entity.Answer;
import com.influy.domain.answer.entity.AnswerType;
import com.influy.domain.answer.repository.AnswerRepository;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.entity.TalkBoxOpenStatus;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.member.service.MemberService;
import com.influy.domain.question.entity.Question;
import com.influy.domain.question.repository.QuestionRepository;
import com.influy.domain.questionTag.entity.QuestionTag;
import com.influy.domain.questionTag.repository.QuestionTagRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final ItemRepository itemRepository;
    private final MemberService memberService;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionTagRepository questionTagRepository;

    @Override
    @Transactional
    public Answer createIndividualAnswer(CustomUserDetails userDetails, Long itemId, Long questionCategoryId, Long questionTagId, Long questionId, AnswerRequestDto.AnswerIndividualDto request, AnswerType answerType) {
        memberService.checkSeller(userDetails);
        Item item = checkTalkBoxOpenStatus(itemId);
        Question question = questionRepository.findValidQuestion(itemId, questionCategoryId, questionTagId, questionId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_INVALID_RELATION));

        Answer answer = AnswerConverter.toAnswer(item, question, request.getAnswerContent(), answerType);
        item.getAnswerList().add(answer);
        question.getAnswerList().add(answer);
        question.setIsAnswered(true);

        return answerRepository.save(answer);
    }

    @Override
    @Transactional(readOnly = true)
    public AnswerResponseDto.AnswerTagListDto getList(CustomUserDetails userDetails, Long itemId, Long questionCategoryId, Long questionTagId) {
        memberService.checkSeller(userDetails);
        QuestionTag questionTag = questionTagRepository.findValidQuestionTag(itemId, questionCategoryId, questionTagId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTIONTAG_INVALID_RELATION));

        List<Answer> answerList = answerRepository.findIndividualAndFaqAnswersByQuestionTagId(questionTagId);
        List<Answer> commonList = answerRepository.findCommonAnswersByQuestionTagId(questionTagId);
        Map<String, Answer> uniqueAnswers = new LinkedHashMap<>();
        for (Answer answer : commonList) uniqueAnswers.putIfAbsent(answer.getContent(), answer);
        List<Answer> commonAnswerList = new ArrayList<>(uniqueAnswers.values());
        answerList.addAll(commonAnswerList);
        return AnswerConverter.toAnswerTagListDto(questionTag, answerList);
    }

    @Override
    @Transactional
    public AnswerResponseDto.AnswerCommonResultDto createCommonAnswers(CustomUserDetails userDetails, Long itemId, Long questionCategoryId, AnswerRequestDto.AnswerCommonDto request) {
        memberService.checkSeller(userDetails);
        Item item = checkTalkBoxOpenStatus(itemId);

        List<Question> questionList= questionRepository.findAllById(request.getQuestionIdList());
        List<Answer> answerList = new ArrayList<>();
        for (Question question : questionList) {
            Answer answer = AnswerConverter.toAnswer(item, question, request.getAnswerContent(), AnswerType.COMMON);
            item.getAnswerList().add(answer);
            question.getAnswerList().add(answer);
            question.setIsAnswered(true);
            answerList.add(answer);
        }

        answerRepository.saveAll(answerList);
        return AnswerConverter.toAnswerCommonResultDto(answerList.size(), answerList);
    }

    @Override
    @Transactional
    public AnswerResponseDto.DeleteResultDto delete(CustomUserDetails userDetails, Long itemId, Long questionCategoryId, AnswerRequestDto.DeleteDto request) {
        memberService.checkSeller(userDetails);

        // 질문을 삭제하면 해당 질문의 모든 답변들도 삭제된다고 가정 (셀러 입장)
        List<Question> questionList= questionRepository.findAllById(request.getQuestionIdList());
        for (Question question : questionList) {
            question.setIsHidden(true);
        }

        return AnswerConverter.toDeleteResultDto(request.getQuestionIdList());
    }

    @Override
    @Transactional
    public AnswerResponseDto.TalkBoxOpenStatusDto changeOpenStatus(CustomUserDetails userDetails, Long itemId, TalkBoxOpenStatus openStatus) {
        memberService.checkSeller(userDetails);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if (openStatus == TalkBoxOpenStatus.INITIAL) throw new GeneralException((ErrorStatus.INVALID_TALKBOX_REQUEST));
        item.setTalkBoxOpenStatus(openStatus);

        return AnswerConverter.toTalkBoxOpenStatusDto(itemId, openStatus);
    }


    private Item checkTalkBoxOpenStatus(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
        if (item.getTalkBoxOpenStatus() != TalkBoxOpenStatus.OPENED) throw new GeneralException(ErrorStatus.TALKBOX_CLOSED);
        return item;
    }

}
