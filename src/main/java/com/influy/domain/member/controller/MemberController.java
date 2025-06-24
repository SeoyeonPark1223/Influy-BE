package com.influy.domain.member.controller;


import com.influy.domain.member.converter.MemberConverter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.dto.MemberResponseDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.service.MemberService;
import com.influy.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원")
@RequestMapping("/register")
public class MemberController {

    private final MemberService memberService;
    //일반 유저 가입
    @PostMapping("/user")
    public ApiResponse<MemberResponseDTO.MemberProfile> resisterUser(@RequestBody MemberRequestDTO.UserJoin requestBody){
        Member member = memberService.joinUser(requestBody);
        MemberResponseDTO.MemberProfile body= MemberConverter.toMemberDTO(member);

        return ApiResponse.onSuccess(body);
    }
    @PostMapping("/seller")
    public ApiResponse<MemberResponseDTO.MemberProfile> resisterSeller(@RequestBody MemberRequestDTO.SellerJoin requestBody){
        Member member = memberService.joinSeller(requestBody);
        MemberResponseDTO.MemberProfile body= MemberConverter.toMemberDTO(member);

        return ApiResponse.onSuccess(body);
    }
}
