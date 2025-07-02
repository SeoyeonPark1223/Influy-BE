package com.influy.domain.like.service;

import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.like.converter.LikeConverter;
import com.influy.domain.like.entity.Like;
import com.influy.domain.like.entity.TargetType;
import com.influy.domain.like.repository.LikeRepository;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.repository.MemberRepository;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return likeRepository.findByMemberAndSellerAndTargetType(member, seller, targetType)
                .orElse(LikeConverter.toLike(member, seller, null, targetType));
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
        return likeRepository.findByMemberAndItemAndTargetType(member, item, targetType)
                .orElse(LikeConverter.toLike(member, null, item, targetType));
    }
}
