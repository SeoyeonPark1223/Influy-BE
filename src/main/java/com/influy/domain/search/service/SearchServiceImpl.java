package com.influy.domain.search.service;

import com.influy.domain.item.converter.ItemConverter;
import com.influy.domain.item.dto.ItemResponseDto;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.like.repository.LikeRepository;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.repository.MemberRepository;
import com.influy.domain.search.converter.SearchConverter;
import com.influy.domain.search.dto.SearchResponseDto;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final MemberRepository memberRepository;
    private final SellerProfileRepository sellerRepository;
    private final ItemRepository itemRepository;
    private final LikeRepository likeRepository;

    @Override
    @Transactional(readOnly = true)
    public SearchResponseDto.SellerPageResultDto searchSeller(CustomUserDetails userDetails, String query, PageRequestDto pageRequest) {
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Pageable pageable = pageRequest.toPageable(Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<SellerProfile> sellerPage = null;

        if (sellerRepository.existsByMemberUsernameContainingOrMemberNicknameContainingOrInstagramContaining(query, query, query)) {
            sellerPage = sellerRepository.findAllByMemberUsernameContainingOrMemberNicknameContainingOrInstagramContaining(query, query, query, pageable);
        }

        List<Long> likeSellers = likeRepository.findLikedSellerIdsByMember(member);

        return SearchConverter.toSellerPageResultDto(sellerPage, likeSellers);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto.HomeItemViewPageDto searchItem(CustomUserDetails userDetails, String query, PageRequestDto pageRequest) {
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Pageable pageable = pageRequest.toPageable(Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Item> itemPage = null;

        if (sellerRepository.existsByMemberUsernameContainingOrMemberNicknameContainingOrInstagramContaining(query, query, query)
                || itemRepository.existsByNameContaining(query)) {
            itemPage = itemRepository.findAllByNameContainingOrSeller_Member_UsernameContainingOrSeller_Member_NicknameContainingOrSeller_InstagramContaining(query, query, query, query, pageable);
        }

        List<Long> likeItems = likeRepository.findLikedItemIdsByMember(member);

        return ItemConverter.toHomeItemViewPageDto(itemPage, likeItems);
    }
}
