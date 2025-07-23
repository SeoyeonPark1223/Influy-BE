package com.influy.domain.sellerProfile.service;

import com.influy.domain.item.dto.jpql.ItemJPQLResponse;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.sellerProfile.dto.SellerProfileRequestDTO;
import com.influy.domain.sellerProfile.entity.ItemSortType;
import com.influy.domain.sellerProfile.entity.SellerProfile;

import java.util.List;

public interface SellerProfileService {

    SellerProfile getSellerProfile(Long memberId);

    SellerProfile updateSeller(SellerProfile sellerProfile, SellerProfileRequestDTO.UpdateProfile requestBody);

    SellerProfile updateItemSortType(SellerProfile sellerProfile, ItemSortType sortBy);

    SellerProfile createSellerProfile(Member member, MemberRequestDTO.SellerJoin request);

    boolean getIsLikedByMember(SellerProfile seller, Member member);

    List<ItemJPQLResponse> getMarketItems(Long sellerId);
}
