package com.influy.global.auth.controller;

import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.auth.dto.AuthResponseDTO;
import com.influy.global.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "카카오 서버에서 오는 redirect uri 받는 api 입니다. 프론트 사용 금지", description = "사용법: 최상단 명세서 설명란의 로그인 주소를 브라우저 주소창에 붙여넣기->응답으로 오는 카카오 아이디 복사->회원가입 api의 requestBody에 붙여넣기")
    public ApiResponse<AuthResponseDTO.KakaoLoginResponse> getKaKaoUser(@RequestParam("code") String code,
                                                                        @RequestParam(name = "error", required = false) String error,
                                                                        @RequestParam(name = "error_description", required = false) String description){

        AuthResponseDTO.KakaoLoginResponse body =  authService.SocialLogIn(code);

        return ApiResponse.onSuccess(body);
    }



}
