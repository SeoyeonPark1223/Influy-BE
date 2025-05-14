package com.influy.domain.seller.Service;

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
public class SellerService {

    private final SellerRepository sellerRepository;

    public Seller getSeller(Long sellerId){
        return sellerRepository.findById(sellerId).orElseThrow(()->new GeneralException(ErrorStatus.SELLER_NOT_FOUND));
    }
}
