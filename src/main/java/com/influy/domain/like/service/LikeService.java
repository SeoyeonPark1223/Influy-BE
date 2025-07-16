package com.influy.domain.like.service;

import com.influy.domain.like.dto.LikeResponseDto;
import com.influy.domain.like.entity.Like;
import com.influy.global.common.PageRequestDto;
import org.springframework.data.domain.Page;

public interface LikeService {
    Like toAddSellerLike(Long sellerId, Long memberId);
    Like toAddItemLike(Long sellerId, Long itemId, Long memberId);
    Like toCancelSellerLike(Long sellerId, Long memberId);
    Like toCancelItemLike(Long sellerId, Long itemId, Long memberId);
    LikeResponseDto.LikeCountDto toCountSellerLikes(Long sellerId);
    LikeResponseDto.LikeCountDto toCountItemLikes(Long sellerId, Long itemId);
    Page<Like> toGetSellerLikePage(Long memberId, PageRequestDto pageRequest);
    Page<Like> toGetItemLikePage(Long memberId, PageRequestDto pageRequest);
}
