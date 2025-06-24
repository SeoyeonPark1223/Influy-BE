package com.influy.domain.sellerProfile.service;

import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.sellerProfile.dto.SellerProfileRequestDTO;
import com.influy.domain.sellerProfile.entity.ItemSortType;
import com.influy.domain.sellerProfile.entity.SellerProfile;

public interface SellerProfileService {

    SellerProfile getSeller(Long sellerId);

    SellerProfile updateSeller(Long sellerId, SellerProfileRequestDTO.UpdateProfile requestBody);

    SellerProfile updateItemSortType(Long sellerId, ItemSortType sortBy);

    void checkItemMatchSeller(Long sellerId, Long itemId);

    SellerProfile createSellerProfile(Member member, MemberRequestDTO.SellerJoin request);
}
