package com.influy.global.auth.controller;

import com.influy.domain.member.repository.MemberRepository;
import com.influy.domain.member.service.MemberService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.auth.dto.AuthRequestDTO;
import com.influy.global.auth.dto.AuthResponseDTO;
import com.influy.global.auth.service.AuthService;
import com.influy.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "소셜 로그인")
@RequestMapping("/oauth")
public class AuthController {

    private final AuthService authService;


    @GetMapping("/kakao")
    @Operation(summary = "카카오 서버에서 오는 redirect uri 받는 api 입니다", description = "로그인 주소는 페이지 맨 위 설명 보시면 있습니다.")
    public ApiResponse<AuthResponseDTO.TokenPair> getKaKaoUser(@RequestParam("code") String code,
                                                             @RequestParam(name = "error", required = false) String error,
                                                             @RequestParam(name = "error_description", required = false) String description){

        AuthResponseDTO.TokenPair body =  authService.kakaoSignIn(code);

        return ApiResponse.onSuccess(body);
    }



}
