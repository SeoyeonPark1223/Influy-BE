package com.influy.domain.member.controller;


import com.influy.domain.member.converter.MemberConverter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.dto.MemberResponseDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.service.MemberService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.jwt.CustomUserDetails;
import com.influy.global.jwt.CustomUserDetailsServiceImpl;
import com.influy.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원")
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    //일반 유저 가입
    @PostMapping("/register/user")
    public ApiResponse<MemberResponseDTO.MemberProfile> registerUser(@RequestBody MemberRequestDTO.UserJoin requestBody){
        Member member = memberService.joinUser(requestBody);
        MemberResponseDTO.MemberProfile body= MemberConverter.toMemberDTO(member);

        return ApiResponse.onSuccess(body);
    }
    @PostMapping("/register/seller")
    public ApiResponse<MemberResponseDTO.MemberProfile> registerSeller(@RequestBody MemberRequestDTO.SellerJoin requestBody){
        Member member = memberService.joinSeller(requestBody);
        MemberResponseDTO.MemberProfile body= MemberConverter.toMemberDTO(member);

        return ApiResponse.onSuccess(body);
    }

    @GetMapping("/{memberId}/profile")
    public ApiResponse<MemberResponseDTO.MemberProfile> getMemberProfile(@PathVariable Long memberId){
        return null;

    }
}
