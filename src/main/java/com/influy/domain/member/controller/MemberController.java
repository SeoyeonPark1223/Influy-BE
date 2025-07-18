package com.influy.domain.member.controller;


import com.influy.domain.member.converter.MemberConverter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.dto.MemberResponseDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.domain.member.service.MemberService;
import com.influy.domain.sellerProfile.service.SellerProfileService;
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
    @Operation(summary = "유저 가입", description = "유저의 경우 관심 카테고리 리스트 안보내면 BAD REQUEST 뜹니다")
    public ApiResponse<AuthResponseDTO.IdAndToken> registerUser(@RequestBody MemberRequestDTO.UserJoin requestBody, HttpServletResponse response) {
        Member member = memberService.joinUser(requestBody, MemberRole.USER);
        TokenPair tokenPair = authService.issueToken(member);
        AuthResponseDTO.IdAndToken body = AuthConverter.toIdAndTokenDto(member.getId(), tokenPair.accessToken());

        CookieUtil.refreshTokenInCookie(response, tokenPair.refreshToken());

        return ApiResponse.onSuccess(body);
    }
    @PostMapping("/register/seller")
    @Operation(summary = "셀러 가입", description = "셀러는 관심 카테고리 안보내도 됩니다(null/필드 생략 가능)")
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

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "헤더에 accessToken 필수")
    public ApiResponse<SuccessStatus> logout(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails userDetails){

        authService.signOut(request,userDetails.getMember());

        return ApiResponse.onSuccess(SuccessStatus.LOGOUT_SUCCESS);
    }


    @GetMapping("/{memberId}/profile")
    @Operation(summary = "유저용 프로필 조회", description = "셀러용은 셀러쪽 api 사용하세요")
    public ApiResponse<MemberResponseDTO.MemberProfile> getMemberProfile(@PathVariable("memberId") Long memberId){

        Member member = memberService.findById(memberId);
        MemberResponseDTO.MemberProfile body = MemberConverter.toMemberDTO(member);

        return ApiResponse.onSuccess(body);

    }

    @DeleteMapping("/delete")
    @Operation(summary = "멤버 탈퇴 API")
    public ApiResponse<SuccessStatus> deleteMember( @AuthenticationPrincipal CustomUserDetails userDetails){
        Member member = userDetails.getMember();
        memberService.deleteMember(member);

        return ApiResponse.onSuccess(SuccessStatus.ACCOUNT_DELETE_SUCCESS);

    }

    @PatchMapping("/profile")
    @Operation(summary = "유저용 프로필 수정 API", description = "셀러도 사용할 수 있지만 유저와 공통된 필드만 있으니 셀러는 셀러쪽 api 사용합니다")
    public ApiResponse<MemberResponseDTO.MemberProfile> patchMember(@RequestBody MemberRequestDTO.UpdateProfile request,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {

        Member member = memberService.findById(userDetails.getId());
        Member updatedMember = memberService.updateMemeber(member, request);
        MemberResponseDTO.MemberProfile body = MemberConverter.toMemberDTO(updatedMember);

        return ApiResponse.onSuccess(body);
    }

    @PatchMapping("/username")
    @Operation(summary = "유저네임(id) 수정 API", description = "유저/셀러의 아이디 수정하는 API")
    public ApiResponse<MemberResponseDTO.MemberProfile> updateUsername(@RequestBody MemberRequestDTO.UpdateUsername request,
                                                                       @AuthenticationPrincipal CustomUserDetails userDetails){

        Member member = memberService.findById(userDetails.getId());
        Member updatedMember = memberService.updateUsername(member,request);
        MemberResponseDTO.MemberProfile body = MemberConverter.toMemberDTO(updatedMember);

        return ApiResponse.onSuccess(body);
    }

    @PostMapping("/register/duplicate-check")
    @Operation(summary = "유저네임(id) 중복 확인")
    public ApiResponse<BaseCode> duplicateCheck(@RequestBody MemberRequestDTO.UsernameDuplicateCheck request){
        if(memberService.checkUsername(request.getUsername())){
            return ApiResponse.onSuccess(ErrorStatus.USERNAME_ALREADY_EXISTS);
        }
        return ApiResponse.onSuccess(SuccessStatus.NO_DUPLICATE_ROW);

    }

}
