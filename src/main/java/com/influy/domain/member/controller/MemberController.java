package com.influy.domain.member.controller;


import com.influy.domain.member.converter.MemberConverter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.dto.MemberResponseDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.service.MemberService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.apiPayload.code.status.SuccessStatus;
import com.influy.global.auth.converter.AuthConverter;
import com.influy.global.auth.dto.AuthResponseDTO;
import com.influy.global.auth.service.AuthService;
import com.influy.global.jwt.CookieUtil;
import com.influy.global.jwt.CustomUserDetails;
import com.influy.global.jwt.CustomUserDetailsServiceImpl;
import com.influy.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원")
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    //일반 유저 가입
    @PostMapping("/register/user")
    public ApiResponse<AuthResponseDTO.IdAndToken> registerUser(@RequestBody MemberRequestDTO.UserJoin requestBody, HttpServletResponse response) {
        Member member = memberService.joinUser(requestBody);
        String[] tokenPair = authService.issueToken(member);
        AuthResponseDTO.IdAndToken body = AuthConverter.toTokenPair(member.getId(), tokenPair[0]);

        CookieUtil.refreshTokenInCookie(response, tokenPair[1]);

        return ApiResponse.onSuccess(body);
    }
    @PostMapping("/register/seller")
    public ApiResponse<AuthResponseDTO.IdAndToken> registerSeller(@RequestBody MemberRequestDTO.SellerJoin requestBody, HttpServletResponse response) {
        Member member = memberService.joinSeller(requestBody);
        String[] tokenPair = authService.issueToken(member);
        AuthResponseDTO.IdAndToken body = AuthConverter.toTokenPair(member.getId(), tokenPair[0]);

        CookieUtil.refreshTokenInCookie(response, tokenPair[1]);

        return ApiResponse.onSuccess(body);
    }

    @GetMapping("/auth/reissue")
    public ApiResponse<AuthResponseDTO.IdAndToken> reissueToken(HttpServletRequest request, HttpServletResponse response){

        String[] tokenPair = authService.reissueToken(request, response);
        Long memberId = jwtTokenProvider.getId(tokenPair[1]);
        AuthResponseDTO.IdAndToken body = AuthConverter.toTokenPair(memberId, tokenPair[0]);

        CookieUtil.refreshTokenInCookie(response, tokenPair[1]);

        return ApiResponse.onSuccess(body);

    }

    @PatchMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails userDetails){

        authService.signOut(request,userDetails.getMember());

        return ApiResponse.onSuccess(SuccessStatus.LOGOUT_SUCCESS);
    }


    @GetMapping("/{memberId}/profile")
    public ApiResponse<MemberResponseDTO.MemberProfile> getMemberProfile(@PathVariable("memberId") Long memberId){

        Member member = memberService.findById(memberId);
        MemberResponseDTO.MemberProfile body = MemberConverter.toMemberDTO(member);

        return ApiResponse.onSuccess(body);

    }

    @DeleteMapping("/{memberId}/delete")
    @Operation(summary = "멤버 탈퇴 임시 API")
    public ApiResponse<String> deleteMember(@PathVariable("memberId") Long memberId){
        Member member = memberService.findById(memberId);
        memberService.deleteMember(member);

        return ApiResponse.onSuccess("정상적으로 탈퇴 되었습니다.");

    }

    @PatchMapping("/profile")
    @Operation(summary = "프로필 수정 API", description = "닉네임과 프로필 사진 수정 API")
    public ApiResponse<MemberResponseDTO.MemberProfile> patchMember(@RequestBody MemberRequestDTO.UpdateProfile request,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {

        //영속화를 위해 findBy로 찾음
        Member member = memberService.findById(userDetails.getId());
        Member updatedMember = memberService.updateMemeber(member, request);
        MemberResponseDTO.MemberProfile body = MemberConverter.toMemberDTO(updatedMember);

        return ApiResponse.onSuccess(body);
    }

    @PatchMapping("/username")
    @Operation(summary = "유저네임(id) 수정 API", description = "유저의 아이디만 수정하는 API")
    public ApiResponse<MemberResponseDTO.MemberProfile> updateUsername(@RequestBody MemberRequestDTO.UpdateUsername request,
                                                                       @AuthenticationPrincipal CustomUserDetails userDetails){

        Member member = memberService.findById(userDetails.getId());
        Member updatedMember = memberService.updateUsername(member,request);
        MemberResponseDTO.MemberProfile body = MemberConverter.toMemberDTO(updatedMember);

        return ApiResponse.onSuccess(body);
    }

}
