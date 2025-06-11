package com.influy.domain.seller.service;

import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.seller.converter.SellerConverter;
import com.influy.domain.seller.dto.SellerRequestDTO;
import com.influy.domain.seller.entity.ItemSortType;
import com.influy.domain.seller.entity.Seller;
import com.influy.domain.seller.repository.SellerRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService{

    private final SellerRepository sellerRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Seller join(SellerRequestDTO.Join requestBody) {
        Seller seller = SellerConverter.toSeller(requestBody);
        return sellerRepository.save(seller);
    }

    public Seller getSeller(Long sellerId){
        return sellerRepository.findById(sellerId).orElseThrow(()->new GeneralException(ErrorStatus.SELLER_NOT_FOUND));
    }

    @Transactional
    public Seller updateSeller(Long sellerId, SellerRequestDTO.UpdateProfile requestBody) {
        Seller seller = getSeller(sellerId);
        return seller.setProfile(requestBody);
    }

    @Transactional
    public Seller updateItemSortType(Long sellerId, ItemSortType sortBy) {
        Seller seller = getSeller(sellerId);
        return seller.setItemSortType(sortBy);
    }

    @Override
    public void checkItemMatchSeller(Long sellerId, Long itemId) {
        Seller seller = getSeller(sellerId);
        Item item = itemRepository.findById(itemId).orElseThrow(()->new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if(!item.getSeller().equals(seller)) {
            throw new GeneralException(ErrorStatus.UNMATCHED_SELLER_ITEM);
        }
    }
}
