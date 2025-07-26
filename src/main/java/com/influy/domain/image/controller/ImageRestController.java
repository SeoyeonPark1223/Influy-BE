package com.influy.domain.image.controller;

import com.influy.domain.image.converter.ImageConverter;
import com.influy.domain.image.dto.ImageRequestDto;
import com.influy.domain.image.dto.ImageResponseDto;
import com.influy.domain.image.service.ImageService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;

@Tag(name = "이미지 업로드", description = "이미지 PresignedURL 발급")
@RestController
@RequiredArgsConstructor
@RequestMapping("image")
public class ImageRestController {
    private final ImageService imageService;

    @PostMapping("/presigned-url")
    @Operation(summary = "이미지 PresignedURL 발급",
            description = "확장자를 포함한 이미지 이름을 입력하면 PresignedURL이 발급됩니다. " +
                    "발급된 PresignedURL로 PUT 요청을 하여 S3에 이미지를 업로드합니다. " +
                    "이미지 업로드에 성공하면 해당 imageURL이 활성화됩니다.")
    ApiResponse<ImageResponseDto.UploadResultDto> uploadImg (@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @RequestBody @Valid ImageRequestDto.UploadDto request) {
        return ApiResponse.onSuccess(imageService.uploadImg(userDetails, request));
    }
}
