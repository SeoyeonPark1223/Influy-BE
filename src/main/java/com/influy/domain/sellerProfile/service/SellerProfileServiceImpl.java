package com.influy.domain.sellerProfile.service;

import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.sellerProfile.converter.SellerProfileConverter;
import com.influy.domain.sellerProfile.dto.SellerProfileRequestDTO;
import com.influy.domain.sellerProfile.entity.ItemSortType;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SellerProfileServiceImpl implements SellerProfileService {

    private final SellerProfileRepository sellerRepository;
    private final ItemRepository itemRepository;
    private final SellerProfileRepository sellerProfileRepository;

    public SellerProfile getSeller(Long sellerId){
        return sellerRepository.findById(sellerId).orElseThrow(()->new GeneralException(ErrorStatus.SELLER_NOT_FOUND));
    }

    @Transactional
    public SellerProfile updateSeller(Long sellerId, SellerProfileRequestDTO.UpdateProfile requestBody) {
        SellerProfile seller = getSeller(sellerId);
        return seller.setProfile(requestBody);
    }

    @Transactional
    public SellerProfile updateItemSortType(Long sellerId, ItemSortType sortBy) {
        SellerProfile seller = getSeller(sellerId);
        return seller.setItemSortType(sortBy);
    }

    @Override
    public void checkItemMatchSeller(Long sellerId, Long itemId) {
        SellerProfile seller = getSeller(sellerId);
        Item item = itemRepository.findById(itemId).orElseThrow(()->new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if(!item.getSeller().equals(seller)) {
            throw new GeneralException(ErrorStatus.UNMATCHED_SELLER_ITEM);
        }
    }

    @Override
    public SellerProfile createSellerProfile(Member member, MemberRequestDTO.SellerJoin request) {

        SellerProfile sellerProfile = SellerProfileConverter.toSellerProfile(member,request);
        member.setSellerProfile(sellerProfile);
        return sellerProfileRepository.save(sellerProfile);
    }
}
