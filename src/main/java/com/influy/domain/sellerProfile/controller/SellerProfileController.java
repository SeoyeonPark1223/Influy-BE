package com.influy.domain.sellerProfile.controller;

import com.influy.domain.member.service.MemberService;
import com.influy.domain.sellerProfile.dto.SellerProfileRequestDTO;
import com.influy.domain.sellerProfile.entity.ItemSortType;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.service.SellerProfileService;
import com.influy.domain.sellerProfile.converter.SellerProfileConverter;
import com.influy.domain.sellerProfile.dto.SellerProfileResponseDTO;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="셀러")
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerProfileController {

    private final SellerProfileService sellerService;
    private final MemberService memberService;


    //프로필 조회
    @GetMapping("/{sellerId}/profile")
    @Operation(summary = "셀러 프로필 조회 API", description = "아무나 사용할 수 있는 조회 API")
    public ApiResponse<SellerProfileResponseDTO.SellerProfile> getSellerProfile(@PathVariable("sellerId") Long sellerId){

        SellerProfile seller = sellerService.getSellerProfile(sellerId);
        SellerProfileResponseDTO.SellerProfile body = SellerProfileConverter.toSellerProfileDTO(seller);

        return ApiResponse.onSuccess(body);
    }

    //프로필 수정
    @PatchMapping("/profile")
    @Operation(summary = "셀러가 본인 프로필 수정 API", description = "수정한 필드만 보내면 됨. 유저 프로필은 회원쪽 api 사용하세요")
    public ApiResponse<SellerProfileResponseDTO.SellerProfile> updateSellerProfile(@RequestBody SellerProfileRequestDTO.UpdateProfile requestBody,
                                                                                   @AuthenticationPrincipal CustomUserDetails userDetails){

        SellerProfile sellerProfile = memberService.checkSeller(userDetails);
        SellerProfileResponseDTO.SellerProfile body = SellerProfileConverter.toSellerProfileDTO(sellerService.updateSeller(sellerProfile,requestBody));

        return ApiResponse.onSuccess(body);
    }

    @PutMapping("/item-sort")
    @Operation(summary = "셀러 마켓 아이템 정렬방식 수정 API")
    public ApiResponse<SellerProfileResponseDTO.SortType> updateItemSort(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                         @RequestParam("sort-type") ItemSortType sortBy){

        SellerProfile sellerProfile = memberService.checkSeller(userDetails);
        SellerProfileResponseDTO.SortType body  = SellerProfileConverter.toSortTypeDTO(sellerService.updateItemSortType(sellerProfile,sortBy));

        return ApiResponse.onSuccess(body);
    }

}
