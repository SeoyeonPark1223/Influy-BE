package com.influy.domain.like.converter;

import com.influy.domain.item.converter.ItemConverter;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.entity.TalkBoxInfoPair;
import com.influy.domain.like.dto.LikeResponseDto;
import com.influy.domain.like.entity.Like;
import com.influy.domain.like.entity.LikeStatus;
import com.influy.domain.like.entity.TargetType;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import org.springframework.data.domain.Page;

import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

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

    public static LikeResponseDto.SellerLikeDto toSellerLikeDto(Like like) {
        SellerProfile seller = like.getSeller();

        return LikeResponseDto.SellerLikeDto.builder()
                .likeId(like.getId())
                .memberId(like.getMember().getId())
                .targetType(like.getTargetType())
                .likeStatus(like.getLikeStatus())
                .sellerId(seller.getId())
                .sellerName(seller.getMember().getNickname())
                .build();
    }

    public static LikeResponseDto.ItemLikeDto toItemLikeDto(Like like) {
        return LikeResponseDto.ItemLikeDto.builder()
                .likeId(like.getId())
                .memberId(like.getMember().getId())
                .targetType(like.getTargetType())
                .likeStatus(like.getLikeStatus())
                .itemId(like.getItem().getId())
                .itemName(like.getItem().getName())
                .build();
    }

    public static LikeResponseDto.LikeCountDto toLikeCountDto(TargetType targetType, Long targetId, Integer cnt) {
        return LikeResponseDto.LikeCountDto.builder()
                .targetId(targetId)
                .targetType(targetType)
                .likeCount(cnt)
                .build();
    }

    public static LikeResponseDto.ViewSellerLikeDto toViewSellerLikeDto(Like like) {
        SellerProfile seller = like.getSeller();
        Member member = seller.getMember();

        return LikeResponseDto.ViewSellerLikeDto.builder()
                .targetType(like.getTargetType())
                .sellerId(seller.getId())
                .nickName(member.getNickname())
                .userName(member.getUsername())
                .profileImgLink(member.getProfileImg())
                .build();
    }

    public static LikeResponseDto.ViewItemLikeDto toViewItemLikeDto(Like like, MemberRole memberRole, TalkBoxInfoPair talkBoxInfoPair) {
        Item item = like.getItem();
        return LikeResponseDto.ViewItemLikeDto.builder()
                .targetType(like.getTargetType())
                .itemPreviewDto(item != null ? ItemConverter.toDetailPreviewDto(
                item,
                true,
                memberRole,
                talkBoxInfoPair.waitingCntMap().getOrDefault(item.getId(), 0),
                talkBoxInfoPair.completedCntMap().getOrDefault(item.getId(), 0)) : null)
                .build();
    }

    public static LikeResponseDto.SellerLikePageDto toSellerLikePageDto(Page<Like> likePage) {
        List<LikeResponseDto.ViewSellerLikeDto> viewLikeList = likePage != null ?
                likePage.stream()
                .map(LikeConverter::toViewSellerLikeDto).toList()
                : Collections.emptyList();

        return LikeResponseDto.SellerLikePageDto.builder()
                .sellerLikeList(viewLikeList)
                .listSize(viewLikeList.size())
                .totalPage(likePage == null ? 0: likePage.getTotalPages())
                .totalElements(likePage == null ? 0: likePage.getTotalElements())
                .isFirst(likePage == null || likePage.isFirst())
                .isLast(likePage == null || likePage.isLast())
                .build();
    }

    public static LikeResponseDto.ItemLikePageDto toItemLikePageDto(Page<Like> likePage, MemberRole memberRole, TalkBoxInfoPair talkBoxInfoPair) {
        List<LikeResponseDto.ViewItemLikeDto> viewLikeList = likePage != null ?
                likePage.stream()
                .map(like -> toViewItemLikeDto(like, memberRole, talkBoxInfoPair)).toList()
                : Collections.emptyList();

        return LikeResponseDto.ItemLikePageDto.builder()
                .itemLikeList(viewLikeList)
                .listSize(viewLikeList.size())
                .totalPage(likePage == null ? 0: likePage.getTotalPages())
                .totalElements(likePage == null ? 0: likePage.getTotalElements())
                .isFirst(likePage == null || likePage.isFirst())
                .isLast(likePage == null || likePage.isLast())
                .build();
    }
}
