package com.influy.domain.seller.service;

import com.influy.domain.profileLink.entity.ProfileLink;
import com.influy.domain.seller.converter.SellerConverter;
import com.influy.domain.seller.dto.SellerRequestDTO;
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

    @Transactional
    public Seller join(SellerRequestDTO.Join requestBody) {
        Seller seller = SellerConverter.toSeller(requestBody);
        return sellerRepository.save(seller);
    }

    public Seller getSeller(Long sellerId){
        return sellerRepository.findById(sellerId).orElseThrow(()->new GeneralException(ErrorStatus.SELLER_NOT_FOUND));
    }
}
