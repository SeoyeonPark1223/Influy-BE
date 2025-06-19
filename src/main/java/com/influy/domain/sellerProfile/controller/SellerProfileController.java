package com.influy.domain.sellerProfile.controller;

import com.influy.domain.sellerProfile.dto.SellerProfileRequestDTO;
import com.influy.domain.sellerProfile.entity.ItemSortType;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.service.SellerProfileService;
import com.influy.domain.sellerProfile.converter.SellerProfileConverter;
import com.influy.domain.sellerProfile.dto.SellerProfileResponseDTO;
import com.influy.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="셀러")
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerProfileController {

    private final SellerProfileService sellerService;


    //프로필 조회
    @GetMapping("/profile")//로그인 구현 이후 엔드포인트 변경
    @Operation(summary = "셀러가 본인 프로필 조회 API", description = "로그인 기능 구현 전까지는 PathVariable으로 id 받아서 사용합니다.")
    public ApiResponse<SellerProfileResponseDTO.SellerProfile> getSellerProfile(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId){

        //로그인 구현 이후 SellerProfile 객체 반환
        SellerProfile seller = sellerService.getSeller(sellerId);

        SellerProfileResponseDTO.SellerProfile body = SellerProfileConverter.toSellerProfileDTO(seller);

        return ApiResponse.onSuccess(body);
    }

    //프로필 수정
    @PatchMapping("/profile")
    @Operation(summary = "셀러가 본인 프로필 수정 API", description = "수정한 필드만 보내면 됨")
    public ApiResponse<SellerProfileResponseDTO.SellerProfile> updateSellerProfile(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                                   @RequestBody SellerProfileRequestDTO.UpdateProfile requestBody){

        SellerProfileResponseDTO.SellerProfile body = SellerProfileConverter.toSellerProfileDTO(sellerService.updateSeller(sellerId,requestBody));

        return ApiResponse.onSuccess(body);
    }

    @PutMapping
    @Operation(summary = "셀러 마켓 아이템 정렬방식 수정 API")
    public ApiResponse<SellerProfileResponseDTO.SortType> updateItemSort(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                         @RequestParam("sortBy") ItemSortType sortBy){

        SellerProfileResponseDTO.SortType body  = SellerProfileConverter.toSortTypeDTO(sellerService.updateItemSortType(sellerId,sortBy));

        return ApiResponse.onSuccess(body);
    }

}
