package com.influy.domain.seller.controller;

import com.influy.domain.seller.dto.SellerRequestDTO;
import com.influy.domain.seller.entity.ItemSortType;
import com.influy.domain.seller.service.SellerService;
import com.influy.domain.seller.service.SellerServiceImpl;
import com.influy.domain.seller.converter.SellerConverter;
import com.influy.domain.seller.dto.SellerResponseDTO;
import com.influy.domain.seller.entity.Seller;
import com.influy.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="셀러")
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;

    //가입
    @PostMapping("/register")
    public ApiResponse<SellerResponseDTO.SellerProfile> resisterSeller(@RequestBody SellerRequestDTO.Join requestBody){
        Seller seller = sellerService.join(requestBody);
        SellerResponseDTO.SellerProfile body= SellerConverter.toSellerProfileDTO(seller);

        return ApiResponse.onSuccess(body);
    }


    //프로필 조회
    @GetMapping("/profile")//로그인 구현 이후 엔드포인트 변경
    @Operation(summary = "셀러가 본인 프로필 조회 API", description = "로그인 기능 구현 전까지는 PathVariable으로 id 받아서 사용합니다.")
    public ApiResponse<SellerResponseDTO.SellerProfile> getSellerProfile(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId){

        //로그인 구현 이후 Seller 객체 반환
        Seller seller = sellerService.getSeller(sellerId);

        SellerResponseDTO.SellerProfile body = SellerConverter.toSellerProfileDTO(seller);

        return ApiResponse.onSuccess(body);
    }

    //프로필 수정
    @PatchMapping("/profile")
    @Operation(summary = "셀러가 본인 프로필 수정 API", description = "수정한 필드만 보내면 됨")
    public ApiResponse<SellerResponseDTO.SellerProfile> updateSellerProfile(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                            @RequestBody SellerRequestDTO.UpdateProfile requestBody){

        SellerResponseDTO.SellerProfile body = SellerConverter.toSellerProfileDTO(sellerService.updateSeller(sellerId,requestBody));

        return ApiResponse.onSuccess(body);
    }

    @PutMapping
    @Operation(summary = "셀러 마켓 아이템 정렬방식 수정 API")
    public ApiResponse<SellerResponseDTO.SortType> updateItemSort(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                  @RequestParam("sortBy") ItemSortType sortBy){

        SellerResponseDTO.SortType body  = SellerConverter.toSortTypeDTO(sellerService.updateItemSortType(sellerId,sortBy));

        return ApiResponse.onSuccess(body);
    }

}
