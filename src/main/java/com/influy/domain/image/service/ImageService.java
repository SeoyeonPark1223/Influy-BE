package com.influy.domain.image.service;

import com.influy.domain.image.dto.ImageRequestDto;
import com.influy.domain.image.dto.ImageResponseDto;
import com.influy.domain.item.entity.Item;
import com.influy.global.jwt.CustomUserDetails;

import java.net.URL;
import java.util.List;

public interface ImageService {
    ImageResponseDto.UploadResultDto uploadImg(CustomUserDetails userDetails, ImageRequestDto.UploadDto request);
//    void deleteItemImg(Item item);
}
