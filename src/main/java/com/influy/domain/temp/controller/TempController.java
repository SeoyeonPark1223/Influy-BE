package com.influy.domain.temp.controller;

import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="임시", description = "임시 테스트 API")
@RestController
@RequiredArgsConstructor
public class TempController {
    @GetMapping("/temp")
    @Operation(summary = "임시 테스트 API")
    public ApiResponse<?> temp() {
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }
}
