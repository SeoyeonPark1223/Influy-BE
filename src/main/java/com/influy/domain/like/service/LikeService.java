package com.influy.domain.like.service;

import com.influy.domain.like.dto.LikeResponseDto;
import com.influy.domain.like.entity.Like;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;
import org.springframework.data.domain.Page;

public interface LikeService {
    Like toAddSellerLike(Long sellerId, Long memberId);
    Like toAddItemLike(Long sellerId, Long itemId, Long memberId);
    Like toCancelSellerLike(Long sellerId, Long memberId);
    Like toCancelItemLike(Long sellerId, Long itemId, Long memberId);
    LikeResponseDto.LikeCountSellerDto toCountSellerLikes(CustomUserDetails userDetails, Long sellerId);
    LikeResponseDto.LikeCountItemDto toCountItemLikes(Long sellerId, Long itemId);
    LikeResponseDto.SellerLikePageDto toGetSellerLikePage(CustomUserDetails userDetails, PageRequestDto pageRequest);
    LikeResponseDto.ItemLikePageDto toGetItemLikePage(CustomUserDetails userDetails, PageRequestDto pageRequest);
}
