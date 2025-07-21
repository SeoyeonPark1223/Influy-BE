package com.influy.global.auth.controller;

import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.auth.dto.AuthResponseDTO;
import com.influy.global.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "소셜 로그인")
@RequestMapping("/oauth")
public class AuthController {

    private final AuthService authService;


    @GetMapping("/kakao")
    @Operation(summary = "카카로 로그인 후 토큰/회원가입 필요 메시지 get", description = "배포 서버는 기본적으로 프론트 로컬호스트로 redirect 하나, 바꾸고 싶을 경우 기재할 것. 단, 상단의 인가코드 받는 주소와 redirectUri가 동일해야함")
    public ApiResponse<AuthResponseDTO.KakaoLoginResponse> getKaKaoUser(@RequestParam("code") String code,
                                                                        @RequestParam(name = "redirectionUri", required = false) String redirectionUri,
                                                                        HttpServletResponse response) {

        AuthResponseDTO.KakaoLoginResponse body =  authService.SocialLogIn(code,response, redirectionUri);

        return ApiResponse.onSuccess(body);
    }



}
