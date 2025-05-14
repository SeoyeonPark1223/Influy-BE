package com.influy.domain.seller.controller;

import com.influy.domain.profileLink.entity.ProfileLink;
import com.influy.domain.profileLink.service.ProfileLinkService;
import com.influy.domain.seller.Service.SellerService;
import com.influy.domain.seller.converter.SellerConverter;
import com.influy.domain.seller.dto.SellerResponseDTO;
import com.influy.domain.seller.entity.Seller;
import com.influy.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;
    private final ProfileLinkService profileLinkService;

    @GetMapping("/profile/{sellerId}")//로그인 구현 이후 엔드포인트 변경
    public ApiResponse<Object> getSellerProfile(/*로그인 구현 이후 Id 말고 Principal 받기*/ @PathVariable Long sellerId){

        //로그인 구현 이후 Seller 객체 반환
        Seller seller = sellerService.getSeller(sellerId);
        List<ProfileLink> profileLinks = profileLinkService.getProfileLinksOf(seller);

        SellerResponseDTO.SellerProfile body = SellerConverter.toSellerProfileDTO(seller,profileLinks);

        return ApiResponse.onSuccess(body);
    }

}
