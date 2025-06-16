package com.influy.domain.image.service;

import com.influy.domain.image.dto.ImageRequestDto;

import java.net.URL;

public interface ImageService {
    URL uploadImg(ImageRequestDto.UploadDto request);
}
