package com.influy.domain.questionTag.service;

import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionTag.converter.QuestionTagConverter;
import com.influy.domain.questionTag.dto.QuestionTagResponseDTO;
import com.influy.domain.questionTag.dto.jpql.TagJPQLResult;
import com.influy.domain.questionTag.entity.QuestionTag;
import com.influy.domain.questionTag.repository.QuestionTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionTagServiceImpl implements QuestionTagService{
    private final QuestionTagRepository questionTagRepository;

    @Override
    @Transactional
    public QuestionTag createTag(String tagName, QuestionCategory questionCategory) {

        QuestionTag tag =  QuestionTagConverter.toQuestionTag(tagName,questionCategory);
        return questionTagRepository.save(tag);
    }

    @Override
    public List<QuestionTagResponseDTO.General> getTagInfoListByCategoryId(Long categoryId, Boolean isAnswered) {
        List<TagJPQLResult.QuestionTagInfo> questionTagList = questionTagRepository.findTagAndCountByCategoryIdOrderByCount(categoryId, isAnswered);

        List<QuestionTagResponseDTO.General> body = questionTagList.stream().map(QuestionTagConverter::toGeneralDTO).collect(Collectors.toList());


        return addCategoryInfo(body);
    }

    //카테고리 '전체' 버튼 용 데이터 추가
    private List<QuestionTagResponseDTO.General> addCategoryInfo(List<QuestionTagResponseDTO.General> body) {
        //전체 버튼 용 데이터
        Long totalQuestions = 0L;
        boolean uncheckedQuestions = false;
        for(QuestionTagResponseDTO.General tagInfo : body){
            totalQuestions+=tagInfo.getTotalQuestions();
            if(tagInfo.isUncheckedExists()){
                uncheckedQuestions= true;
            }
        }
        QuestionTagResponseDTO.General totalDTO= QuestionTagConverter.toGeneralTotalDTO(null,"전체",totalQuestions,uncheckedQuestions);

        body.addFirst(totalDTO);

        return body;
    }
}
