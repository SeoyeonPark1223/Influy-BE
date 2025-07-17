package com.influy.domain.image.service;

import com.influy.domain.image.dto.ImageRequestDto;
import com.influy.domain.image.dto.ImageResponseDto;

import java.net.URL;

public interface ImageService {
    ImageResponseDto.UploadResultDto uploadImg(ImageRequestDto.UploadDto request);
}
