package com.influy.domain.profileLink.controller;

import com.influy.domain.profileLink.converter.ProfileLinkConverter;
import com.influy.domain.profileLink.dto.ProfileLinkRequestDTO;
import com.influy.domain.profileLink.dto.ProfileLinkResponseDTO;
import com.influy.domain.profileLink.entity.ProfileLink;
import com.influy.domain.profileLink.service.ProfileLinkService;
import com.influy.domain.profileLink.service.ProfileLinkServiceImpl;
import com.influy.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "링크 인 바이오")
@RequestMapping("/seller/market-links")
public class ProfileLinkController {
    private final ProfileLinkService profileLinkServiceImpl;
    //링크 생성
    @PostMapping("/{sellerId}")
    @Operation(summary = "링크 추가",description = "링크를 추가합니다.")
    public ApiResponse<ProfileLinkResponseDTO.General> createLink(@PathVariable("sellerId")Long sellerId, @RequestBody ProfileLinkRequestDTO request){

        ProfileLink profileLink = profileLinkServiceImpl.createLinkOf(sellerId, request);

        ProfileLinkResponseDTO.General body = ProfileLinkConverter.toGeneralDTO(profileLink);

        return ApiResponse.onSuccess(body);
    }

    //링크 수정
    @PatchMapping("/{sellerId}")
    @Operation(summary = "링크 수정",description = "링크를 수정합니다.")
    public ApiResponse<ProfileLinkResponseDTO.General> updateLink(@PathVariable("sellerId")Long sellerId, @RequestBody ProfileLinkRequestDTO request){

        ProfileLink profileLink = profileLinkServiceImpl.updateLinkOf(sellerId, request);

        ProfileLinkResponseDTO.General body = ProfileLinkConverter.toGeneralDTO(profileLink);

        return ApiResponse.onSuccess(body);
    }

    //링크 리스트 조회
    @GetMapping("/{sellerId}")//얘는 로그인 구현 후에도 pathVariable로 남겨두기
    @Operation(summary = "링크 리스트 조회",description = "특정 셀러의 링크 리스트를 조회합니다.")
    public ApiResponse<Page<ProfileLinkResponseDTO.General>> getLinkList(@PathVariable("sellerId") Long sellerId){

        Page<ProfileLink> profileLinks = profileLinkServiceImpl.getLinkListOf(sellerId);

        Page<ProfileLinkResponseDTO.General> body = profileLinks.map(ProfileLinkConverter::toGeneralDTO);

        return ApiResponse.onSuccess(body);
    }

    //링크 삭제

    @PostMapping("/{linkId}")
    @Operation(summary = "링크 삭제",description = "링크를 삭제합니다.")
    public ApiResponse<String> deleteLink(@PathVariable("linkId")Long linkId){

        profileLinkServiceImpl.deleteLink(linkId);

        return ApiResponse.onSuccess("삭제에 성공했습니다.");
    }
}
