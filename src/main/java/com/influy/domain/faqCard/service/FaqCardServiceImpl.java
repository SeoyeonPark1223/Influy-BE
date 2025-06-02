package com.influy.domain.faqCard.service;

import com.influy.domain.faqCard.converter.FaqCardConverter;
import com.influy.domain.faqCard.dto.FaqCardRequestDto;
import com.influy.domain.faqCard.entity.FaqCard;
import com.influy.domain.faqCard.repository.FaqCardRepository;
import com.influy.domain.faqCategory.converter.FaqCategoryConverter;
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
    private final FaqCategoryRepository faqCategoryRepository;
    private final FaqCardRepository faqCardRepository;
    private final SellerRepository sellerRepository;

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

    @Override
    @Transactional
    public FaqCard create(Long sellerId, Long itemId, Long faqCategoryId, FaqCardRequestDto.CreateDto request) {
        FaqCategory faqCategory = checkAll(sellerId, itemId, faqCategoryId);
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SELLER_NOT_FOUND));

        FaqCard faqCard = FaqCardConverter.toFaqCard(request, faqCategory, seller);
        seller.getFaqCardList().add(faqCard);
        faqCategory.getFaqCardList().add(faqCard);
        return faqCardRepository.save(faqCard);
    }

    @Override
    @Transactional(readOnly = true)
    public FaqCard getAnswerCard(Long sellerId, Long itemId, Long faqCardId) {
        FaqCard faqCard = faqCardRepository.findById(faqCardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FAQ_CARD_NOT_FOUND));
        checkAll(sellerId, itemId, faqCard.getFaqCategory().getId());

        return faqCard;
    }

    FaqCategory checkAll (Long sellerId, Long itemId, Long faqCategoryId) {
        FaqCategory faqCategory = faqCategoryRepository.findById(faqCategoryId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FAQ_CATEGORY_NOT_FOUND));

        Item item = faqCategory.getItem();
        if (!item.getId().equals(itemId)) {
            throw new GeneralException(ErrorStatus.UNMATCHED_ITEM_FAQCATEGORY);
        }

        Seller seller = item.getSeller();
        if (!seller.getId().equals(sellerId)) {
            throw new GeneralException(ErrorStatus.UNMATCHED_SELLER_ITEM);
        }

        return faqCategory;
    }
}
