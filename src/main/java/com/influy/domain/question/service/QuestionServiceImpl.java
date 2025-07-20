package com.influy.domain.question.service;

import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.question.entity.Question;
import com.influy.domain.question.repository.QuestionRepository;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.repository.QuestionCategoryRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionCategoryRepository questionCategoryRepository;

    @Override
    public Page<Question> getQuestionsByCategory(Long questionCategoryId, Boolean isAnswered, Pageable pageable) {

        QuestionCategory questionCategory = questionCategoryRepository.findById(questionCategoryId).orElseThrow(
                ()->new GeneralException(ErrorStatus.QUESTION_CATEGORY_NOT_FOUND));

        return questionRepository.findAllByQuestionCategoryAndIsAnswered(questionCategory,isAnswered,pageable);
    }


}
