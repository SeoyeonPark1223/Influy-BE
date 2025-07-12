package com.influy.domain.like.service;

import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.like.converter.LikeConverter;
import com.influy.domain.like.dto.LikeResponseDto;
import com.influy.domain.like.entity.Like;
import com.influy.domain.like.entity.LikeStatus;
import com.influy.domain.like.entity.TargetType;
import com.influy.domain.like.repository.LikeRepository;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.repository.MemberRepository;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.common.PageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final SellerProfileRepository sellerRepository;
    private final ItemRepository itemRepository;
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Like toAddSellerLike(Long sellerId, Long memberId) {
        TargetType targetType = TargetType.SELLER;
        SellerProfile seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SELLER_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Optional<Like> optLike = likeRepository.findByMemberIdAndSellerIdAndTargetType(memberId, sellerId, targetType);

        if (optLike.isPresent()) {
            Like like = optLike.get();
            if (like.getLikeStatus() == LikeStatus.UNLIKE) {
                like.setLikeStatus(LikeStatus.LIKE);
                return likeRepository.save(like);
            }
            return like;
        }

        Like like = LikeConverter.toLike(member, seller, null, targetType);
        member.getLikeList().add(like);
        seller.getLikeList().add(like);
        return likeRepository.save(like);
    }

    @Override
    @Transactional
    public Like toAddItemLike(Long sellerId, Long itemId, Long memberId) {
        TargetType targetType = TargetType.ITEM;
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
        if (!item.getSeller().getId().equals(sellerId)) throw new GeneralException(ErrorStatus.UNMATCHED_SELLER_ITEM);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Optional<Like> optLike = likeRepository.findByMemberIdAndItemIdAndTargetType(memberId, itemId, targetType);

        if (optLike.isPresent()) {
            Like like = optLike.get();
            if (like.getLikeStatus() == LikeStatus.UNLIKE) {
                like.setLikeStatus(LikeStatus.LIKE);
                return likeRepository.save(like);
            }
            return like;
        }

        Like like = LikeConverter.toLike(member, null, item, targetType);
        member.getLikeList().add(like);
        item.getLikeList().add(like);
        return likeRepository.save(like);
    }

    @Override
    @Transactional
    public Like toCancelSellerLike(Long sellerId, Long memberId) {
        Like like = likeRepository.findByMemberIdAndSellerIdAndTargetType(memberId, sellerId, TargetType.SELLER)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LIKE_NOT_FOUND));

        like.setLikeStatus(LikeStatus.UNLIKE);
        return likeRepository.save(like);
    }

    @Override
    @Transactional
    public Like toCancelItemLike(Long sellerId, Long itemId, Long memberId) {
        Like like = likeRepository.findByMemberIdAndItemIdAndTargetType(memberId, itemId, TargetType.ITEM)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LIKE_NOT_FOUND));

        like.setLikeStatus(LikeStatus.UNLIKE);
        return likeRepository.save(like);
    }

    @Override
    @Transactional(readOnly = true)
    public LikeResponseDto.LikeCountDto toCountSellerLikes(Long sellerId) {
        // like repository에서 sellerId, likeStatus like 개수를 count해서 가져와야함
        Integer likeCnt = likeRepository.countBySellerIdAndTargetTypeAndLikeStatus(sellerId, TargetType.SELLER, LikeStatus.LIKE);
        return LikeConverter.toLikeCountDto(TargetType.SELLER, sellerId, likeCnt);
    }

    @Override
    @Transactional(readOnly = true)
    public LikeResponseDto.LikeCountDto toCountItemLikes(Long sellerId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
        if (!item.getSeller().getId().equals(sellerId)) throw new GeneralException(ErrorStatus.UNMATCHED_SELLER_ITEM);

        // like repository에서 itemId, likeStatus like 개수를 count해서 가져와야함
        Integer likeCnt = likeRepository.countByItemIdAndTargetTypeAndLikeStatus(itemId, TargetType.ITEM, LikeStatus.LIKE);
        return LikeConverter.toLikeCountDto(TargetType.ITEM, itemId, likeCnt);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Like> toGetSellerLikePage(Long memberId, PageRequestDto pageRequest) {
        // 정렬: 최근 상품 올린 셀러가 위로 가도록 (seller -> 가장 최근 updatedAt 아이템 기준 정렬)
        return likeRepository.findSellerLikesOrderByRecentItem(memberId, pageRequest.toPageable());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Like> toGetItemLikePage(Long memberId, PageRequestDto pageRequest) {
        // 정렬: 아이템 마감일 빠른순
        Pageable pageable = pageRequest.toPageable(Sort.by("item.endDate").ascending());
        return likeRepository.findByMemberIdAndTargetTypeAndLikeStatus(memberId, TargetType.ITEM, LikeStatus.LIKE, pageable);
    }
}
