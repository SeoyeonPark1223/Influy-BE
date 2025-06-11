package com.influy.domain.seller.service;

import com.influy.domain.seller.dto.SellerRequestDTO;
import com.influy.domain.seller.entity.ItemSortType;
import com.influy.domain.seller.entity.Seller;

public interface SellerService {
    Seller join(SellerRequestDTO.Join requestBody);

    Seller getSeller(Long sellerId);

    Seller updateSeller(Long sellerId, SellerRequestDTO.UpdateProfile requestBody);

    Seller updateItemSortType(Long sellerId, ItemSortType sortBy);

    void checkItemMatchSeller(Long sellerId, Long itemId);
}
