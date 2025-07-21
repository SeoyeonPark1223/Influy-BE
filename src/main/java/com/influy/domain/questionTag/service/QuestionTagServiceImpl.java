package com.influy.domain.questionTag.service;

import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionTag.converter.QuestionTagConverter;
import com.influy.domain.questionTag.entity.QuestionTag;
import com.influy.domain.questionTag.repository.QuestionTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionTagServiceImpl implements QuestionTagService{
    private final QuestionTagRepository questionTagRepository;

    @Override
    @Transactional
    public QuestionTag createTag(String tagName, QuestionCategory questionCategory) {

        QuestionTag tag =  QuestionTagConverter.toQuestionTag(tagName,questionCategory);
        System.out.println("컨버터까지 성공:"+tag.getName());
        return questionTagRepository.save(tag);
    }
}
