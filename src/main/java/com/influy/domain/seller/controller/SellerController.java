package com.influy.domain.seller.controller;

import com.influy.domain.profileLink.entity.ProfileLink;
import com.influy.domain.profileLink.service.ProfileLinkService;
import com.influy.domain.seller.dto.SellerRequestDTO;
import com.influy.domain.seller.service.SellerService;
import com.influy.domain.seller.converter.SellerConverter;
import com.influy.domain.seller.dto.SellerResponseDTO;
import com.influy.domain.seller.entity.Seller;
import com.influy.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;
    private final ProfileLinkService profileLinkService;

    @PostMapping("/resister")
    public ApiResponse<SellerResponseDTO.SellerProfile> resisterSeller(@RequestBody SellerRequestDTO.Join requestBody){
        Seller seller = sellerService.join(requestBody);
        SellerResponseDTO.SellerProfile body= SellerConverter.toSellerProfileDTO(seller);

        return ApiResponse.onSuccess(body);
    }


    @GetMapping("/profile/{sellerId}")//로그인 구현 이후 엔드포인트 변경
    public ApiResponse<SellerResponseDTO.SellerProfile> getSellerProfile(/*로그인 구현 이후 Id 말고 Principal 받기*/ @PathVariable Long sellerId){

        //로그인 구현 이후 Seller 객체 반환
        Seller seller = sellerService.getSeller(sellerId);
        List<ProfileLink> profileLinks = profileLinkService.getProfileLinksOf(seller);

        SellerResponseDTO.SellerProfile body = SellerConverter.toSellerProfileDTO(seller);

        return ApiResponse.onSuccess(body);
    }

}
