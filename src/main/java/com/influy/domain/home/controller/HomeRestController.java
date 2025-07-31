package com.influy.domain.home.controller;

import com.influy.domain.home.dto.HomeResponseDto;
import com.influy.domain.home.service.HomeService;
import com.influy.domain.member.service.MemberService;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "홈", description = "홈 관련 API")
@RestController
@RequiredArgsConstructor
public class HomeRestController {
    private final MemberService memberService;
    private final HomeService homeService;

    @GetMapping("/home/close-deadline")
    @Operation(summary = "홈 마감 임박 상품 페이지", description = "24시간 이내 마감 상품")
    public ApiResponse<HomeResponseDto.HomeItemViewPageDto> getCloseDeadline(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                             @Valid @ParameterObject PageRequestDto pageRequest) {
        return ApiResponse.onSuccess(homeService.getCloseDeadline(userDetails, pageRequest));
    }

    @GetMapping("/home/popular")
    @Operation(summary = "홈 인기 급상승 상품 페이지", description = "3개 반환 (나중에 더보기 생길 것을 고려해 pageable 사용)")
    public ApiResponse<HomeResponseDto.HomeItemViewPageDto> getPopular(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                       @Valid @ParameterObject PageRequestDto pageRequest) {
        return ApiResponse.onSuccess(homeService.getPopular(userDetails, pageRequest));
    }

    @GetMapping("/home/recommend")
    @Operation(summary = "홈 추천 상품 페이지", description = "itemCategory request param 없이 요청보내면 '전체' 카테고리 결과 반환")
    public ApiResponse<HomeResponseDto.HomeItemViewPageDto> getRecommended(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                           @Valid @ParameterObject PageRequestDto pageRequest,
                                                                           @RequestParam(name = "categoryId", required = false) Long categoryId) {
        return ApiResponse.onSuccess(homeService.getRecommended(userDetails, pageRequest, categoryId));
    }

    @GetMapping("seller/home/questions")
    @Operation(summary = "셀러 홈 상품 질문 안내", description = "아이템 + 아이템 별 많이 들어오는 질문 카테고리")
    public ApiResponse<HomeResponseDto.SellerHomeItemPageDTO> getSellerHomeItem(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                @Valid @ParameterObject PageRequestDto pageRequestDto){
        SellerProfile seller = memberService.checkSeller(userDetails);
        return ApiResponse.onSuccess(homeService.getSellerHomeItemWithQuestionStatus(seller, pageRequestDto));
    }

    @GetMapping("/home/trending-seller")
    @Operation(summary = "요즘 핫한 셀러", description = "랜덤 정렬")
    public ApiResponse<HomeResponseDto.SellerThumbnailListDto> getTrendingSeller() {
        return ApiResponse.onSuccess(homeService.getTrendingSeller());
    }

    @GetMapping("/home/{sellerId}/pick")
    @Operation(summary = "셀러가 픽한 상품", description = "셀러의 아이템 메인 이미지 3개")
    public ApiResponse<HomeResponseDto.SellerPick3Dto> getPicked(@PathVariable("sellerId") Long sellerId) {
        return ApiResponse.onSuccess(homeService.getPicked(sellerId));
    }

}
