package com.influy.domain.home.service;

import com.influy.domain.home.dto.HomeResponseDto;
import com.influy.domain.item.dto.ItemResponseDto;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;

import java.util.List;

public interface HomeService {
    HomeResponseDto.HomeItemViewPageDto getCloseDeadline(CustomUserDetails userDetails, PageRequestDto pageRequest);

    HomeResponseDto.HomeItemViewPageDto getPopular(CustomUserDetails userDetails, PageRequestDto pageRequest);

    HomeResponseDto.HomeItemViewPageDto getRecommended(CustomUserDetails userDetails, PageRequestDto pageRequest, Long categoryId);

    HomeResponseDto.SellerHomeItemPageDTO getSellerHomeItemWithQuestionStatus(SellerProfile seller, PageRequestDto pageRequestDto);

    HomeResponseDto.SellerThumbnailListDto getTrendingSeller();

    HomeResponseDto.SellerPick3Dto getPicked(Long sellerId);

    List<Long> getLikeItems(CustomUserDetails userDetails);
}
