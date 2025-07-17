package com.influy.domain.question.service;

import com.influy.domain.ai.service.AiService;
import com.influy.domain.ai.service.converter.AiConverter;
import com.influy.domain.ai.service.dto.AiRequestDTO;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.member.entity.Member;
import com.influy.domain.question.entity.Question;
import com.influy.domain.question.repository.QuestionRepository;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.repository.QuestionCategoryRepository;
import com.influy.domain.questionTag.converter.QuestionTagConverter;
import com.influy.domain.questionTag.dto.QuestionTagResponseDTO;
import com.influy.domain.questionTag.entity.QuestionTag;
import com.influy.domain.questionTag.repository.QuestionTagRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final QuestionTagRepository questionTagRepository;
    private final AiService aiService;

    @Override
    public Page<Question> getQuestionList(QuestionCategory category, Pageable pageable) {
        return questionRepository.findAllByQuestionCategory(category,pageable);
    }

    @Override
    public Page<Question> getQuestionsByCategory(Long questionCategoryId, Boolean isAnswered, Pageable pageable) {

        QuestionCategory questionCategory = questionCategoryRepository.findById(questionCategoryId).orElseThrow(
                ()->new GeneralException(ErrorStatus.QUESTION_CATEGORY_NOT_FOUND));

        return questionRepository.findAllByQuestionCategoryAndIsAnswered(questionCategory,isAnswered,pageable);
    }

    @Override
    public Question createQuestion(Member member, Long questionCategoryId, String content) {

        QuestionCategory questionCategory = questionCategoryRepository.findById(questionCategoryId)
                .orElseThrow(()->new GeneralException(ErrorStatus.QUESTION_CATEGORY_NOT_FOUND));

        String answer = aiService.classifyQuestion(content, questionCategory);

        return null;
    }


}
