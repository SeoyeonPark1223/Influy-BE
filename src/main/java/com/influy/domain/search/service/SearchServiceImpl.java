package com.influy.domain.search.service;

import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.item.service.ItemService;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.repository.MemberRepository;
import com.influy.domain.search.converter.SearchConverter;
import com.influy.domain.search.dto.SearchRequestDto;
import com.influy.domain.search.dto.SearchResponseDto;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.domain.sellerProfile.service.SellerProfileService;
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
    private final ItemService itemService;
    private final SellerProfileService sellerService;

    @Override
    @Transactional(readOnly = true)
    public SearchResponseDto.SearchResultDto search(CustomUserDetails userDetails, String query, PageRequestDto sellerPageRequest, PageRequestDto itemPageRequest) {
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Pageable pageableSeller = sellerPageRequest.toPageable(Sort.by(Sort.Direction.DESC, "createdAt"));
        Pageable pageableItem = itemPageRequest.toPageable(Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<SellerProfile> sellerPage = null;
        Page<Item> itemPage = null;

        if (sellerRepository.existsByMemberUsernameContainingOrMemberNicknameContainingOrInstagramContaining(query, query, query)
                && itemRepository.existsByNameContaining(query)) {
            sellerPage = sellerRepository.findAllByMemberUsernameContainingOrMemberNicknameContainingOrInstagramContaining(query, query, query, pageableSeller);
            itemPage = itemRepository.findAllByNameContaining(query, pageableItem);
        }
        if (sellerRepository.existsByMemberUsernameContainingOrMemberNicknameContainingOrInstagramContaining(query, query, query)) {
            sellerPage = sellerRepository.findAllByMemberUsernameContainingOrMemberNicknameContainingOrInstagramContaining(query, query, query, pageableSeller);
            itemPage = itemRepository.findAllBySellerIn(sellerPage.getContent(), pageableItem);
        }
        else if (itemRepository.existsByNameContaining(query)) {
            itemPage = itemRepository.findAllByNameContaining(query, pageableItem);
        }

        List<Long> likeSellers = sellerService.getLikeSellers(member);
        List<Long> likeItems = itemService.getLikeItems(member);

        return SearchConverter.toSearchResultDto(sellerPage, itemPage, likeSellers, likeItems);
    }
}
