package com.influy.domain.faqCategory.service;

import com.influy.domain.faqCard.repository.FaqCardRepository;
import com.influy.domain.faqCategory.converter.FaqCategoryConverter;
import com.influy.domain.faqCategory.dto.FaqCategoryRequestDto;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.faqCategory.repository.FaqCategoryRepository;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.member.service.MemberService;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaqCategoryServiceImpl implements FaqCategoryService {
    private final SellerProfileRepository sellerRepository;
    private final ItemRepository itemRepository;
    private final FaqCategoryRepository faqCategoryRepository;
    private final FaqCardRepository faqCardRepository;
    private final MemberService memberService;


    @Override
    @Transactional
    public FaqCategory add(CustomUserDetails userDetails, Long itemId, FaqCategoryRequestDto.AddDto request) {
        memberService.checkSeller(userDetails);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        int nextNum = faqCategoryRepository.countAllByItemId(itemId) + 1;

        // 중복 체크
        boolean exists = faqCategoryRepository.existsByItemIdAndCategory(itemId, request.getCategory());
        if (exists) throw new GeneralException(ErrorStatus.FAQ_CATEGORY_ALREADY_EXISTS);

        FaqCategory newFaqCategory= FaqCategoryConverter.toFaqCategory(request, item, nextNum);
        item.getFaqCategoryList().add(newFaqCategory);

        return faqCategoryRepository.save(newFaqCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FaqCategory> getList(Long sellerId, Long itemId) {
        if (!sellerRepository.existsById(sellerId)) throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        if (!itemRepository.existsById(itemId)) throw new GeneralException(ErrorStatus.ITEM_NOT_FOUND);

        Sort sort = Sort.by(Sort.Direction.ASC, "categoryOrder");

        return faqCategoryRepository.findAllByItemId(itemId, sort);
    }

    @Override
    @Transactional
    public void delete(CustomUserDetails userDetails, Long itemId, FaqCategoryRequestDto.DeleteDto request) {
        memberService.checkSeller(userDetails);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        FaqCategory faqCategory = faqCategoryRepository.findById(request.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.FAQ_CATEGORY_NOT_FOUND));

        item.getFaqCategoryList().remove(faqCategory);
        faqCardRepository.deleteAllByFaqCategory(faqCategory); // 해당 faq 카테고리에 있는 모든 faq 카드 삭제
        faqCategoryRepository.delete(faqCategory);

        // 순서 정리
        List<FaqCategory> faqCategoryList = faqCategoryRepository.findAllByItemId(itemId, Sort.by(Sort.Direction.ASC, "categoryOrder"));
        for (int i = 0; i < faqCategoryList.size(); i++) {
            faqCategoryList.get(i).setCategoryOrder(i + 1);
        }
    }

    @Override
    @Transactional
    public FaqCategory update(CustomUserDetails userDetails, Long itemId, FaqCategoryRequestDto.UpdateDto request) {
        memberService.checkSeller(userDetails);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        FaqCategory faqCategory = faqCategoryRepository.findById(request.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.FAQ_CATEGORY_NOT_FOUND));

        if (!faqCategory.getItem().getId().equals(item.getId())) throw new GeneralException(ErrorStatus.INVALID_FAQ_ITEM_RELATION);

        if (request.getCategory() != null) faqCategory.setCategory(request.getCategory());
        return faqCategory;
    }

    @Override
    @Transactional
    public List<FaqCategory> updateOrderAll(CustomUserDetails userDetails, Long itemId, FaqCategoryRequestDto.UpdateOrderDto request) {
        memberService.checkSeller(userDetails);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        List<Long> idList= request.getIds();
        if (idList.size() != faqCategoryRepository.countAllByItemId(itemId)) throw new GeneralException(ErrorStatus.UPDATE_ORDER_INVALID_FORMAT);

        List<FaqCategory> updatedList = new ArrayList<>();

        Map<Long, FaqCategory> updateMap = faqCategoryRepository.findAllByItem(item).stream()
                .collect(Collectors.toMap(FaqCategory::getId, Function.identity()));

        for (int i=0; i<idList.size(); i++) {
            Long id = idList.get(i);
            FaqCategory faqCategory = updateMap.get(id);
            if (faqCategory == null) throw new GeneralException(ErrorStatus.FAQ_CATEGORY_NOT_FOUND);

            if (faqCategory.getCategoryOrder().equals(i+1)) {
                updatedList.add(faqCategory);
                continue;
            }
            faqCategory.setCategoryOrder(i + 1);
            updatedList.add(faqCategory);
        }

        return updatedList;
    }
}
