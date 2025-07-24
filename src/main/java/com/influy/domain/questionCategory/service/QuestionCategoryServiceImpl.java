package com.influy.domain.questionCategory.service;

import com.influy.domain.ai.service.AiService;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.entity.TalkBoxOpenStatus;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.member.service.MemberService;
import com.influy.domain.question.repository.QuestionRepository;
import com.influy.domain.questionCategory.converter.QuestionCategoryConverter;
import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDto;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDto;
import com.influy.domain.questionCategory.dto.jpql.CategoryJPQLResult;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.repository.QuestionCategoryRepository;
import com.influy.domain.questionTag.converter.QuestionTagConverter;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionCategoryServiceImpl implements QuestionCategoryService{

    private final ItemRepository itemRepository;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final SellerProfileRepository sellerRepository;
    private final AiService aiService;
    private final MemberService memberService;
    private final QuestionRepository questionRepository;

    @Override
    @Transactional
    public List<QuestionCategory> addAll(CustomUserDetails userDetails, Long itemId, QuestionCategoryRequestDto.AddListDto request) {
        memberService.checkSeller(userDetails);
        Item item = checkInitial(itemId);

        List<QuestionCategory> questionCategoryList = new ArrayList<>();
        for (String newCategory : request.getCategoryList()) {
            // 중복 체크
            boolean exists = questionCategoryRepository.existsByItemIdAndName(itemId, newCategory);
            if (exists) throw new GeneralException(ErrorStatus.FAQ_CATEGORY_ALREADY_EXISTS);

            // 추가
            QuestionCategory questionCategory = QuestionCategoryConverter.toQuestionCategory(item, newCategory);
            item.getQuestionCategoryList().add(questionCategory);
            questionCategoryList.add(questionCategory);
            questionCategoryRepository.save(questionCategory);
            questionCategory.getQuestionTagList().add(QuestionTagConverter.toQuestionTag("기타", questionCategory));
        }

        return questionCategoryList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryJPQLResult.CategoryInfo> getList(Long sellerId, Boolean isAnswered, Long itemId) {
        checkSellerAndItem(sellerId, itemId);

        // 정렬 순서: 질문 많은 순
        return questionCategoryRepository.findQuestionCategories(itemId, isAnswered);
    }

    @Override
    @Transactional
    public List<String> generateCategory(CustomUserDetails userDetails, Long itemId) {
        memberService.checkSeller(userDetails);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        return aiService.generateCategory(item);
    }

    @Override
    public QuestionCategory findByCategoryIdAndItemId(Long questionCategoryId, Long itemId) {

        return questionCategoryRepository.findByIdAndItemId(questionCategoryId, itemId).orElseThrow(()->new GeneralException(ErrorStatus.QUESTION_CATEGORY_NOT_FOUND));
    }

    @Override
    public List<CategoryJPQLResult.IsAnswered> getIsAnsweredMap(Long categoryId, Long itemId) {

        if(categoryId!=null){
            return questionRepository.countIsAnsweredByCategoryId(categoryId);
        }else if(itemId!=null){
            return questionRepository.countIsAnsweredByItemId(itemId);
        }
        return null;
    }

    private void checkSellerAndItem(Long sellerId, Long itemId) {
        if (!itemRepository.existsByIdAndSellerId(itemId, sellerId)){
            throw new GeneralException(ErrorStatus.NOT_OWNER);
        }
    }

    private Item checkInitial(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if (item.getTalkBoxOpenStatus() != TalkBoxOpenStatus.INITIAL) throw new GeneralException(ErrorStatus.TALKBOX_ALREADY_OPENED);

        return item;
    }
}
