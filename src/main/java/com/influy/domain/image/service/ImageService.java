package com.influy.domain.image.service;

import com.influy.domain.image.dto.ImageRequestDto;
import com.influy.domain.image.dto.ImageResponseDto;
import com.influy.global.jwt.CustomUserDetails;

import java.net.URL;

public interface ImageService {
    ImageResponseDto.UploadResultDto uploadImg(CustomUserDetails userDetails, ImageRequestDto.UploadDto request);
}
