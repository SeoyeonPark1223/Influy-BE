package com.influy.domain.profileLink.service;

import com.influy.domain.profileLink.dto.ProfileLinkRequestDTO;
import com.influy.domain.profileLink.entity.ProfileLink;
import com.influy.domain.sellerProfile.entity.SellerProfile;

import java.util.List;

public interface ProfileLinkService {

    ProfileLink createLinkOf(SellerProfile seller, ProfileLinkRequestDTO request);

    ProfileLink updateLinkOf(SellerProfile seller, Long linkId, ProfileLinkRequestDTO request);

    List<ProfileLink> getLinkListOf(Long sellerId);

    void deleteLinkOf(SellerProfile seller, Long linkId);
}
