package com.influy.domain.like.converter;

import com.influy.domain.item.entity.Item;
import com.influy.domain.like.dto.LikeResponseDto;
import com.influy.domain.like.entity.Like;
import com.influy.domain.like.entity.LikeStatus;
import com.influy.domain.like.entity.TargetType;
import com.influy.domain.member.entity.Member;
import com.influy.domain.sellerProfile.entity.SellerProfile;

public class LikeConverter {
    public static Like toLike(Member member, SellerProfile seller, Item item, TargetType targetType) {
        return Like.builder()
                .member(member)
                .seller(seller) // Nullable
                .item(item) // Nullable
                .likeStatus(LikeStatus.LIKE)
                .targetType(targetType)
                .build();
    }

    public static LikeResponseDto.AddSellerLikeDto toAddSellerLikeDto(Like like) {
        SellerProfile seller = like.getSeller();

        return LikeResponseDto.AddSellerLikeDto.builder()
                .memberId(like.getMember().getId())
                .targetType(like.getTargetType())
                .sellerId(seller.getId())
                .sellerName(seller.getMember().getNickname())
                .build();
    }

    public static LikeResponseDto.AddItemLikeDto toAddItemLikeDto(Like like) {
        return LikeResponseDto.AddItemLikeDto.builder()
                .memberId(like.getMember().getId())
                .targetType(like.getTargetType())
                .itemId(like.getItem().getId())
                .itemName(like.getItem().getName())
                .build();
    }
}
