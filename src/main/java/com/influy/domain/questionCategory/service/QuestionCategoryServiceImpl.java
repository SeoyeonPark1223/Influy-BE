package com.influy.domain.questionCategory.service;

import com.influy.domain.ai.service.AiService;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.entity.TalkBoxOpenStatus;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.questionCategory.converter.QuestionCategoryConverter;
import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDto;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDto;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.repository.QuestionCategoryRepository;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionCategoryServiceImpl implements QuestionCategoryService{

    private final ItemRepository itemRepository;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final SellerProfileRepository sellerRepository;
    private final AiService aiService;

    @Override
    @Transactional
    public QuestionCategory add(Long sellerId, Long itemId, QuestionCategoryRequestDto.AddDto request) {
        Item item = checkSellerAndItem(sellerId, itemId);
        if (item.getTalkBoxOpenStatus() != TalkBoxOpenStatus.INITIAL) throw new GeneralException(ErrorStatus.TALKBOX_ALREADY_OPENED);

        // 중복 체크
        boolean exists = questionCategoryRepository.existsByItemIdAndCategory(itemId, request.getCategory());
        if (exists) throw new GeneralException(ErrorStatus.FAQ_CATEGORY_ALREADY_EXISTS);

        // 추가
        QuestionCategory questionCategory = QuestionCategoryConverter.toQuestionCategory(item, request.getCategory());
        item.getQuestionCategoryList().add(questionCategory);

        return questionCategoryRepository.save(questionCategory);
    }

    @Override
    @Transactional
    public QuestionCategory update(Long sellerId, Long itemId, QuestionCategoryRequestDto.UpdateDto request) {
        Item item = checkSellerAndItem(sellerId, itemId);
        if (item.getTalkBoxOpenStatus() != TalkBoxOpenStatus.INITIAL) throw new GeneralException(ErrorStatus.TALKBOX_ALREADY_OPENED);

        QuestionCategory questionCategory = questionCategoryRepository.findById(request.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_CATEGORY_NOT_FOUND));

        if (!questionCategory.getItem().equals(item))
            throw new GeneralException(ErrorStatus.INVALID_QUESTION_ITEM_RELATION);

        if (request.getCategory() != null) questionCategory.setCategory(request.getCategory());

        return questionCategory;
    }

    @Override
    @Transactional
    public void delete(Long sellerId, Long itemId, QuestionCategoryRequestDto.DeleteDto request) {
        Item item = checkSellerAndItem(sellerId, itemId);
        if (item.getTalkBoxOpenStatus() != TalkBoxOpenStatus.INITIAL) throw new GeneralException(ErrorStatus.TALKBOX_ALREADY_OPENED);

        QuestionCategory questionCategory = questionCategoryRepository.findById(request.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.QUESTION_CATEGORY_NOT_FOUND));

        item.getQuestionCategoryList().remove(questionCategory);
        questionCategoryRepository.delete(questionCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionCategoryResponseDto.ListDto getList(Long sellerId, Long itemId) {
        checkSellerAndItem(sellerId, itemId);

        // 정렬 순서: 질문 많은 순
        List<QuestionCategory> questionCategoryList = questionCategoryRepository.findQuestionCategories(itemId);

        // 질문 개수 map 조회
        Map<Long, Integer> questionCntMap = questionCategoryList.stream()
                .collect(Collectors.toMap(
                        QuestionCategory::getId,
                        qc -> questionCategoryRepository.countQuestionsByCategoryId(qc.getId())
                ));

        // 미확인 질문 수: 0으로 채운 map (미완료)
        Map<Long, Integer> unCheckedCntMap = questionCategoryList.stream()
                .collect(Collectors.toMap(
                        QuestionCategory::getId,
                        qc -> 0
                ));

        return QuestionCategoryConverter.toListDto(questionCategoryList, questionCntMap, unCheckedCntMap);
    }

    @Override
    @Transactional
    public List<QuestionCategory> generateCategory(Long sellerId, Long itemId) {
        Item item = checkSellerAndItem(sellerId, itemId);
        aiService.generateCategory(item);
        return questionCategoryRepository.findAllByItem(item);
    }

    private Item checkSellerAndItem(Long sellerId, Long itemId) {
        if (!sellerRepository.existsById(sellerId)) throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
    }
}
