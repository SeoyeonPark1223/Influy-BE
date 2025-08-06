package com.influy.domain.sellerProfile.controller;

import com.influy.domain.home.dto.HomeResponseDto;
import com.influy.domain.item.dto.jpql.ItemJPQLResponse.IsArchivedItemCount;
import com.influy.domain.item.service.ItemService;
import com.influy.domain.member.entity.Member;
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

import java.util.List;

@RestController
@Tag(name="셀러")
@RequiredArgsConstructor
public class SellerProfileController {

    private final SellerProfileService sellerService;
    private final MemberService memberService;
    private final ItemService itemService;

    //일반 유저의 프로필 조회
    @GetMapping("user/{sellerId}/market")
    @Operation(summary = "셀러 마켓 조회 API", description = "일반 사용자가 셀러 마켓 들어갔을 때")
    public ApiResponse<SellerProfileResponseDTO.MarketProfile> getSellerMarket(@PathVariable("sellerId") Long sellerId) {

        SellerProfile seller = sellerService.getSellerProfile(sellerId);
        Long publicItems = Long.valueOf(itemService.getCount(seller.getId(),false));
        Long reviews = 0L;

        SellerProfileResponseDTO.MarketProfile body = SellerProfileConverter.toMarketProfileDTO(seller, publicItems, reviews);

        return ApiResponse.onSuccess(body);
    }

    //프로필 조회
    @GetMapping("seller/profile")
    @Operation(summary = "셀러 프로필 수정 시 기본값 조회 API", description = "셀러 본인만 가능")
    public ApiResponse<SellerProfileResponseDTO.SellerProfile> getSellerProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {


        SellerProfile seller = memberService.checkSeller(userDetails);
        SellerProfileResponseDTO.SellerProfile body = SellerProfileConverter.toSellerProfileDTO(seller);

        return ApiResponse.onSuccess(body);
    }

    //본인이 프로필 조회
    @GetMapping("seller/my-market")
    @Operation(summary = "셀러 마켓 조회 API", description = "셀러 본인이 셀러 페이지 들어갔을 때")
    public ApiResponse<SellerProfileResponseDTO.MarketProfile> getMyMarket(@AuthenticationPrincipal CustomUserDetails userDetails) {

        SellerProfile seller = memberService.checkSeller(userDetails);

        List<IsArchivedItemCount> itemCountList = sellerService.getMarketItems(seller.getId());
        Long reviews = 0L;

        SellerProfileResponseDTO.MarketProfile body = SellerProfileConverter.toMarketProfileDTO(seller, itemCountList, reviews);

        return ApiResponse.onSuccess(body);
    }



    //프로필 수정
    @PatchMapping("seller/profile")
    @Operation(summary = "셀러가 본인 프로필 수정 API", description = "수정한 필드만 보내면 됨. 유저 프로필은 회원쪽 api 사용하세요")
    public ApiResponse<SellerProfileResponseDTO.SellerProfile> updateSellerProfile(@RequestBody SellerProfileRequestDTO.UpdateProfile requestBody,
                                                                                   @AuthenticationPrincipal CustomUserDetails userDetails){

        SellerProfile sellerProfile = memberService.checkSeller(userDetails);
        SellerProfileResponseDTO.SellerProfile body = SellerProfileConverter.toSellerProfileDTO(sellerService.updateSeller(sellerProfile,requestBody));

        return ApiResponse.onSuccess(body);
    }

    @PutMapping("seller/item-sort")
    @Operation(summary = "셀러 마켓 아이템 정렬방식 수정 API")
    public ApiResponse<SellerProfileResponseDTO.SortType> updateItemSort(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                         @RequestParam("sort-type") ItemSortType sortBy){

        SellerProfile sellerProfile = memberService.checkSeller(userDetails);
        SellerProfileResponseDTO.SortType body  = SellerProfileConverter.toSortTypeDTO(sellerService.updateItemSortType(sellerProfile,sortBy));

        return ApiResponse.onSuccess(body);
    }

    @PutMapping("seller/isPublic")
    @Operation(summary = "셀러 마켓 비공개 여부 수정 API", description = "비공개 시 홈에 안뜸")
    public ApiResponse<SellerProfileResponseDTO.IsPublic> updateIsPublic(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                         @RequestParam("status") Boolean status){

        SellerProfile seller = memberService.checkSeller(userDetails);
        SellerProfileResponseDTO.IsPublic body = SellerProfileConverter.toIsPublicDTO(sellerService.updateIsPublic(seller,status));

        return ApiResponse.onSuccess(body);
    }

    @GetMapping("seller/{sellerId}/overview")
    @Operation(summary = "셀러 오버뷰 정보")
    public ApiResponse<HomeResponseDto.SellerThumbnailDto> getOverview(@PathVariable("sellerId") Long sellerId) {
        return ApiResponse.onSuccess(sellerService.getOverview(sellerId));
    }
}
