package com.influy.domain.faqCategory.service;

import com.influy.domain.faqCategory.converter.FaqCategoryConverter;
import com.influy.domain.faqCategory.dto.FaqCategoryRequestDto;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.faqCategory.repository.FaqCategoryRepository;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqCategoryServiceImpl implements FaqCategoryService {
    private final SellerRepository sellerRepository;
    private final ItemRepository itemRepository;
    private final FaqCategoryRepository faqCategoryRepository;


    @Override
    @Transactional
    public List<FaqCategory> addAll(Long sellerId, Long itemId, List<FaqCategoryRequestDto.AddDto> requestList) {
        if (!sellerRepository.existsById(sellerId)) throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        List<FaqCategory> faqCategoryList = new ArrayList<>();

        for (FaqCategoryRequestDto.AddDto request : requestList) {
            FaqCategory newFaqCategory= FaqCategoryConverter.toFaqCategory(request, item);
            faqCategoryList.add(newFaqCategory);
            item.getFaqCategoryList().add(newFaqCategory);
        }

        return faqCategoryRepository.saveAll(faqCategoryList);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FaqCategory> getPage(Long sellerId, Long itemId, Integer pageNumber) {
        if (!sellerRepository.existsById(sellerId)) throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        if (!itemRepository.existsById(itemId)) throw new GeneralException(ErrorStatus.ITEM_NOT_FOUND);

        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "categoryOrder"));

        return faqCategoryRepository.findAllByItemId(itemId, pageable);
    }

    @Override
    @Transactional
    public void deleteAll(Long sellerId, Long itemId, List<FaqCategoryRequestDto.DeleteDto> requestList) {
        if (!sellerRepository.existsById(sellerId)) throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        for (FaqCategoryRequestDto.DeleteDto request : requestList) {
            FaqCategory faqCategory = faqCategoryRepository.findById(request.getId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.FAQ_CATEGORY_NOT_FOUND));

            item.getFaqCategoryList().remove(faqCategory);
            faqCategoryRepository.delete(faqCategory);
        }
    }

    @Override
    @Transactional
    public List<FaqCategory> updateAll(Long sellerId, Long itemId, List<FaqCategoryRequestDto.UpdateDto> requestList) {
        if (!sellerRepository.existsById(sellerId)) {
            throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        List<FaqCategory> updatedList = new ArrayList<>();

        for (FaqCategoryRequestDto.UpdateDto request : requestList) {
            FaqCategory faqCategory = faqCategoryRepository.findById(request.getId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.FAQ_CATEGORY_NOT_FOUND));

            if (!faqCategory.getItem().getId().equals(item.getId())) {
                throw new GeneralException(ErrorStatus.INVALID_FAQ_ITEM_RELATION);
            }

            if (request.getCategory() != null) faqCategory.setCategory(request.getCategory());
            if (request.getCategoryOrder() != null) faqCategory.setCategoryOrder(request.getCategoryOrder());

            updatedList.add(faqCategory);
        }

        return updatedList;
    }
}
