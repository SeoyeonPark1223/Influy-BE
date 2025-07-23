package com.influy.domain.sellerProfile.service;

import com.influy.domain.item.dto.jpql.ItemJPQLResponse;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.like.entity.LikeStatus;
import com.influy.domain.like.repository.LikeRepository;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.repository.MemberRepository;
import com.influy.domain.member.service.MemberService;
import com.influy.domain.sellerProfile.converter.SellerProfileConverter;
import com.influy.domain.sellerProfile.dto.SellerProfileRequestDTO;
import com.influy.domain.sellerProfile.entity.ItemSortType;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SellerProfileServiceImpl implements SellerProfileService {

    private final ItemRepository itemRepository;
    private final SellerProfileRepository sellerProfileRepository;
    private final LikeRepository likeRepository;

    public SellerProfile getSellerProfile(Long sellerId){
        return sellerProfileRepository.findById(sellerId).orElseThrow(()->new GeneralException(ErrorStatus.SELLER_NOT_FOUND));
    }

    @Override
    @Transactional
    public SellerProfile updateSeller(SellerProfile sellerProfile, SellerProfileRequestDTO.UpdateProfile request) {
        if(request.getProfile()!=null){
            Member member = sellerProfile.getMember();
            member.updateProfile(request.getProfile());
        }
        return sellerProfile.setProfile(request);
    }

    @Transactional
    public SellerProfile updateItemSortType(SellerProfile sellerProfile, ItemSortType sortBy) {

        return sellerProfile.setItemSortType(sortBy);
    }

    @Override
    public void checkItemMatchSeller(Long itemId, Long sellerId) {
        SellerProfile seller = getSellerProfile(sellerId);
        Item item = itemRepository.findById(itemId).orElseThrow(()->new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if(!item.getSeller().equals(seller)) {
            throw new GeneralException(ErrorStatus.UNMATCHED_SELLER_ITEM);
        }
    }

    @Override
    @Transactional
    public SellerProfile createSellerProfile(Member member, MemberRequestDTO.SellerJoin request) {

        SellerProfile sellerProfile = SellerProfileConverter.toSellerProfile(member,request);
        member.setSellerProfile(sellerProfile);
        return sellerProfileRepository.save(sellerProfile);
    }

    @Override
    public boolean getIsLikedByMember(SellerProfile seller, Member member) {

        return likeRepository.existsByMemberAndSellerAndLikeStatus(member,seller,LikeStatus.LIKE);
    }

    @Override
    public List<ItemJPQLResponse> getMarketItems(Long sellerId) {
        return itemRepository.countBySellerIdGroupByIsArchived(sellerId);
    }
}
