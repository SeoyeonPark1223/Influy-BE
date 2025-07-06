package com.influy.global.auth.controller;

import com.influy.domain.member.repository.MemberRepository;
import com.influy.domain.member.service.MemberService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.auth.dto.AuthRequestDTO;
import com.influy.global.auth.dto.AuthResponseDTO;
import com.influy.global.auth.service.AuthService;
import com.influy.global.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "소셜 로그인")
@RequestMapping("/oauth")
public class AuthController {

    private final AuthService authService;


    @GetMapping("/kakao")
    public ApiResponse<AuthResponseDTO.TokenPair> kakaoLogIn(@RequestParam("code") String code,
                                                             @RequestParam(name = "error", required = false) String error,
                                                             @RequestParam(name = "error_description", required = false) String description){

        AuthResponseDTO.TokenPair body =  authService.kakaoSignIn(code);

        return ApiResponse.onSuccess(body);
    }


}
