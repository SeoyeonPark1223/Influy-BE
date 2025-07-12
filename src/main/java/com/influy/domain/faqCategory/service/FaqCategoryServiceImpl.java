package com.influy.domain.faqCategory.service;

import com.influy.domain.faqCard.repository.FaqCardRepository;
import com.influy.domain.faqCategory.converter.FaqCategoryConverter;
import com.influy.domain.faqCategory.dto.FaqCategoryRequestDto;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.faqCategory.repository.FaqCategoryRepository;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.common.PageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqCategoryServiceImpl implements FaqCategoryService {
    private final SellerProfileRepository sellerRepository;
    private final ItemRepository itemRepository;
    private final FaqCategoryRepository faqCategoryRepository;
    private final FaqCardRepository faqCardRepository;


    @Override
    @Transactional
    public FaqCategory add(Long sellerId, Long itemId, FaqCategoryRequestDto.AddDto request) {
        if (!sellerRepository.existsById(sellerId)) throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        int nextNum = 0;
        if (faqCategoryRepository.count() != 0) nextNum = faqCategoryRepository.findMaxOrder();

        // 중복 체크
        boolean exists = faqCategoryRepository.existsByItemIdAndCategory(itemId, request.getCategory());
        if (exists) throw new GeneralException(ErrorStatus.FAQ_CATEGORY_ALREADY_EXISTS);

        // 순서: nextNum +1 부터 시작
        nextNum += 1;
        FaqCategory newFaqCategory= FaqCategoryConverter.toFaqCategory(request, item, nextNum);
        item.getFaqCategoryList().add(newFaqCategory);

        return faqCategoryRepository.save(newFaqCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FaqCategory> getPage(Long sellerId, Long itemId, PageRequestDto pageRequest) {
        if (!sellerRepository.existsById(sellerId)) throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        if (!itemRepository.existsById(itemId)) throw new GeneralException(ErrorStatus.ITEM_NOT_FOUND);

        Pageable pageable = pageRequest.toPageable(Sort.by(Sort.Direction.ASC, "categoryOrder"));

        return faqCategoryRepository.findAllByItemId(itemId, pageable);
    }

    @Override
    @Transactional
    public void delete(Long sellerId, Long itemId, FaqCategoryRequestDto.DeleteDto request) {
        if (!sellerRepository.existsById(sellerId)) throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        FaqCategory faqCategory = faqCategoryRepository.findById(request.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.FAQ_CATEGORY_NOT_FOUND));

        item.getFaqCategoryList().remove(faqCategory);
        faqCardRepository.deleteAllByFaqCategory(faqCategory); // 해당 faq 카테고리에 있는 모든 faq 카드 삭제
        faqCategoryRepository.delete(faqCategory);

        // 순서 정리
        List<FaqCategory> faqCategoryList = faqCategoryRepository.findAllByItemIdAndCategoryOrderAsc(itemId);
        for (int i = 0; i < faqCategoryList.size(); i++) {
            faqCategoryList.get(i).setCategoryOrder(i + 1);
        }
    }

    @Override
    @Transactional
    public FaqCategory update(Long sellerId, Long itemId, FaqCategoryRequestDto.UpdateDto request) {
        if (!sellerRepository.existsById(sellerId)) throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        FaqCategory faqCategory = faqCategoryRepository.findById(request.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.FAQ_CATEGORY_NOT_FOUND));

        if (!faqCategory.getItem().getId().equals(item.getId())) {
            throw new GeneralException(ErrorStatus.INVALID_FAQ_ITEM_RELATION);
        }

        if (request.getCategory() != null) faqCategory.setCategory(request.getCategory());
        faqCategoryRepository.save(faqCategory);

        return faqCategory;
    }

    @Override
    @Transactional
    public List<FaqCategory> updateOrderAll(Long sellerId, Long itemId, List<FaqCategoryRequestDto.UpdateOrderDto> requestList) {
        if (!sellerRepository.existsById(sellerId)) {
            throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        List<FaqCategory> updatedList = new ArrayList<>();

        for (FaqCategoryRequestDto.UpdateOrderDto request : requestList) {
            FaqCategory faqCategory = faqCategoryRepository.findById(request.getId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.FAQ_CATEGORY_NOT_FOUND));

            if (!faqCategory.getItem().getId().equals(item.getId())) {
                throw new GeneralException(ErrorStatus.INVALID_FAQ_ITEM_RELATION);
            }

            if (request.getCategoryOrder() != null) {
                Integer newOrder = request.getCategoryOrder();
                Integer oldOrder = faqCategory.getCategoryOrder();

                if (!newOrder.equals(oldOrder)) {
                    faqCategoryRepository.incrementOrdersFrom(item.getId(), newOrder, oldOrder);
                    faqCategory.setCategoryOrder(newOrder);
                }
            }

            updatedList.add(faqCategory);
        }

        return updatedList;
    }
}
