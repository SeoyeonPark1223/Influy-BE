package com.influy.domain.like.service;

import com.influy.domain.like.entity.Like;

public interface LikeService {
    Like toAddSellerLike(Long sellerId, Long memberId);
    Like toAddItemLike(Long sellerId, Long itemId, Long memberId);
}
