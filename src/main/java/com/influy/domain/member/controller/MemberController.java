package com.influy.domain.member.controller;


import com.influy.domain.member.converter.MemberConverter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.dto.MemberResponseDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.service.MemberService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.apiPayload.code.BaseCode;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.code.status.SuccessStatus;
import com.influy.global.auth.TokenPair;
import com.influy.global.auth.converter.AuthConverter;
import com.influy.global.auth.dto.AuthResponseDTO;
import com.influy.global.auth.service.AuthService;
import com.influy.global.jwt.CookieUtil;
import com.influy.global.jwt.CustomUserDetails;
import com.influy.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
        TokenPair tokenPair = authService.issueToken(member);
        AuthResponseDTO.IdAndToken body = AuthConverter.toIdAndTokenDto(member.getId(), tokenPair.accessToken());

        CookieUtil.refreshTokenInCookie(response, tokenPair.refreshToken());

        return ApiResponse.onSuccess(body);
    }
    @PostMapping("/register/seller")
    public ApiResponse<AuthResponseDTO.IdAndToken> registerSeller(@RequestBody MemberRequestDTO.SellerJoin requestBody, HttpServletResponse response) {
        Member member = memberService.joinSeller(requestBody);
        TokenPair tokenPair = authService.issueToken(member);
        AuthResponseDTO.IdAndToken body = AuthConverter.toIdAndTokenDto(member.getId(), tokenPair.accessToken());

        CookieUtil.refreshTokenInCookie(response, tokenPair.refreshToken());

        return ApiResponse.onSuccess(body);
    }

    @GetMapping("/auth/reissue")
    public ApiResponse<AuthResponseDTO.IdAndToken> reissueToken(HttpServletRequest request, HttpServletResponse response){

        TokenPair tokenPair = authService.reissueToken(request, response);
        Long memberId = jwtTokenProvider.getId(tokenPair.refreshToken());
        AuthResponseDTO.IdAndToken body = AuthConverter.toIdAndTokenDto(memberId, tokenPair.accessToken());

        CookieUtil.refreshTokenInCookie(response, tokenPair.refreshToken());

        return ApiResponse.onSuccess(body);

    }

    @PatchMapping("/logout")
    public ApiResponse<SuccessStatus> logout(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails userDetails){

        authService.signOut(request,userDetails.getMember());

        return ApiResponse.onSuccess(SuccessStatus.LOGOUT_SUCCESS);
    }


    @GetMapping("/{memberId}/profile")
    public ApiResponse<MemberResponseDTO.MemberProfile> getMemberProfile(@PathVariable("memberId") Long memberId){

        Member member = memberService.findById(memberId);
        MemberResponseDTO.MemberProfile body = MemberConverter.toMemberDTO(member);

        return ApiResponse.onSuccess(body);

    }

    @DeleteMapping("/delete")
    @Operation(summary = "멤버 탈퇴 임시 API")
    public ApiResponse<SuccessStatus> deleteMember( @AuthenticationPrincipal CustomUserDetails userDetails){
        Member member = userDetails.getMember();
        memberService.deleteMember(member);

        return ApiResponse.onSuccess(SuccessStatus.ACCOUNT_DELETE_SUCCESS);

    }

    @PatchMapping("/profile")
    @Operation(summary = "프로필 수정 API", description = "닉네임과 프로필 사진 수정 API")
    public ApiResponse<MemberResponseDTO.MemberProfile> patchMember(@RequestBody MemberRequestDTO.UpdateProfile request,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {

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

    @PostMapping("/register/duplicate-check")
    @Operation(summary = "유저네임(id) 중복 확인")
    public ApiResponse<BaseCode> duplicateCheck(@RequestBody MemberRequestDTO.UsernameDuplicateCheck request){
        if(memberService.checkUsername(request.getUsername())){
            return ApiResponse.onSuccess(ErrorStatus.USERNAME_ALREADY_EXISTS);
        }
        return ApiResponse.onSuccess(SuccessStatus.NO_DUPLICATE_ROW);

    }

}
