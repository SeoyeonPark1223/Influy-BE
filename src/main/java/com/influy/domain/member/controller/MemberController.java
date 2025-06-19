package com.influy.domain.member.controller;


import com.influy.domain.member.converter.MemberConverter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.dto.MemberResponseDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.service.MemberService;
import com.influy.domain.sellerProfile.converter.SellerProfileConverter;
import com.influy.domain.sellerProfile.dto.SellerProfileRequestDTO;
import com.influy.domain.sellerProfile.dto.SellerProfileResponseDTO;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원")
public class MemberController {

    private final MemberService memberService;
    //가입
    @PostMapping("/register")
    public ApiResponse<MemberResponseDTO.MemberProfile> resisterSeller(@RequestBody MemberRequestDTO.Join requestBody){
        Member member = memberService.join(requestBody);
        MemberResponseDTO.MemberProfile body= MemberConverter.toMemberDTO(member);

        return ApiResponse.onSuccess(body);
    }
}
