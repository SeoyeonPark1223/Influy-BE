package com.influy.global.auth.controller;

import com.influy.global.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;


}
