package com.influy.domain.faqCard.service;

import com.influy.domain.faqCard.entity.FaqCard;
import com.influy.domain.faqCard.repository.FaqCardRepository;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.faqCategory.repository.FaqCategoryRepository;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.seller.entity.Seller;
import com.influy.domain.seller.repository.SellerRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FaqCardServiceImpl implements FaqCardService {
    private final SellerRepository sellerRepository;
    private final ItemRepository itemRepository;
    private final FaqCategoryRepository faqCategoryRepository;
    private final FaqCardRepository faqCardRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<FaqCard> getPage(Long sellerId, Long itemId, Long faqCategoryId, Integer pageNumber) {
        checkAll(sellerId, itemId, faqCategoryId);

        // questionCard 중에 해당 faqCategory를 가지고 있는 카드들을 가지고 와서 페이지로 반환
        // 정렬순서: (1) 핀된 질문 카드가 앞으로 오도록, (2) 등록이 빠른 질문 카드가 앞으로 오도록
        int pageSize = 10;
        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by(Sort.Order.desc("isPinned"), Sort.Order.asc("createdAt"))
        );

        return faqCardRepository.findByFaqCategoryId(faqCategoryId, pageable);
    }

    void checkAll (Long sellerId, Long itemId, Long faqCategoryId) {
        // seller가 레포에 존재하는지 확인
        // seller가 해당 item을 가지고 있는지 확인
        // item이 해당 faqCategory를 가지고 있는지 확인
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SELLER_NOT_FOUND));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
        if (seller != item.getSeller()) throw new GeneralException(ErrorStatus.UNMATCHED_SELLER_ITEM);
        FaqCategory faqCategory = faqCategoryRepository.findById(faqCategoryId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FAQ_CATEGORY_NOT_FOUND));
        if (item != faqCategory.getItem()) throw new GeneralException(ErrorStatus.UNMATCHED_ITEM_FAQCATEGORY);
    }
}
