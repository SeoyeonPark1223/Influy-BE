package com.influy.domain.questionCategory.service;

import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.item.service.ItemService;
import com.influy.domain.question.repository.QuestionRepository;
import com.influy.domain.questionCategory.converter.QuestionCategoryConverter;
import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDTO;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.repository.QuestionCategoryRepository;
import com.influy.domain.seller.entity.Seller;
import com.influy.domain.seller.service.SellerService;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionCategoryServiceIml implements QuestionCategoryService{

    private final SellerService sellerService;
    private final ItemRepository itemRepository;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final QuestionRepository questionRepository;

    @Override
    public QuestionCategory createCategory(Long sellerId, Long itemId, QuestionCategoryRequestDTO.Create request) {
        //자격 검증
        Seller seller = sellerService.getSeller(sellerId);
        Item item = itemRepository.findById(itemId).orElseThrow(()->new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if(!item.getSeller().equals(seller)) {
            throw new GeneralException(ErrorStatus.FORBIDDEN);
        }

        //생성 로직
        QuestionCategory category = QuestionCategoryConverter.toEntity(item, request);

        return questionCategoryRepository.save(category);

        //item.getQuestionCategoryList().add(category)

    }
}
