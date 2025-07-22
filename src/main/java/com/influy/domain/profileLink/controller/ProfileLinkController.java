package com.influy.domain.profileLink.controller;

import com.influy.domain.member.service.MemberService;
import com.influy.domain.profileLink.converter.ProfileLinkConverter;
import com.influy.domain.profileLink.dto.ProfileLinkRequestDTO;
import com.influy.domain.profileLink.dto.ProfileLinkResponseDTO;
import com.influy.domain.profileLink.entity.ProfileLink;
import com.influy.domain.profileLink.service.ProfileLinkService;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.service.SellerProfileService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "링크 인 바이오")
public class ProfileLinkController {
    private final ProfileLinkService profileLinkServiceImpl;
    private final SellerProfileService sellerService;
    private final MemberService memberService;
    //링크 생성
    @PostMapping("/seller/market-links")
    @Operation(summary = "링크 추가",description = "링크를 추가합니다.")
    public ApiResponse<ProfileLinkResponseDTO.General> createLink(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @RequestBody ProfileLinkRequestDTO request){

        SellerProfile seller = memberService.checkSeller(userDetails);
        ProfileLink profileLink = profileLinkServiceImpl.createLinkOf(seller, request);

        ProfileLinkResponseDTO.General body = ProfileLinkConverter.toGeneralDTO(profileLink);

        return ApiResponse.onSuccess(body);
    }

    //링크 수정
    @PatchMapping("/seller/market-links/{linkId}")
    @Operation(summary = "링크 수정",description = "링크를 수정합니다.")
    public ApiResponse<ProfileLinkResponseDTO.General> updateLink(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @PathVariable("linkId") Long linkId,
                                                                  @RequestBody ProfileLinkRequestDTO request){

        SellerProfile seller = memberService.checkSeller(userDetails);
        ProfileLink profileLink = profileLinkServiceImpl.updateLinkOf(seller, linkId, request);

        ProfileLinkResponseDTO.General body = ProfileLinkConverter.toGeneralDTO(profileLink);

        return ApiResponse.onSuccess(body);
    }

    //링크 리스트 조회
    @GetMapping("/seller/{sellerId}/market-links")//얘는 로그인 구현 후에도 pathVariable로 남겨두기
    @Operation(summary = "링크 리스트 조회",description = "특정 셀러의 링크 리스트를 조회합니다.")
    public ApiResponse<List<ProfileLinkResponseDTO.General>> getLinkList(@PathVariable("sellerId") Long sellerId,
                                                                         @P){

        List<ProfileLink> profileLinks = profileLinkServiceImpl.getLinkListOf(sellerId);

        List<ProfileLinkResponseDTO.General> body = profileLinks.stream().map(ProfileLinkConverter::toGeneralDTO).toList();

        return ApiResponse.onSuccess(body);
    }

    //링크 삭제

    @DeleteMapping("/seller/market-links/{linkId}")
    @Operation(summary = "링크 삭제",description = "링크를 삭제합니다.")
    public ApiResponse<String> deleteLink(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable("linkId") Long linkId){

        SellerProfile seller = memberService.checkSeller(userDetails);
        profileLinkServiceImpl.deleteLinkOf(seller,linkId);

        return ApiResponse.onSuccess("삭제에 성공했습니다.");
    }
}
