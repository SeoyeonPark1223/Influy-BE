package com.influy.domain.profileLink.service;

import com.influy.domain.profileLink.dto.ProfileLinkRequestDTO;
import com.influy.domain.profileLink.entity.ProfileLink;
import com.influy.domain.seller.entity.Seller;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProfileLinkService {

    List<ProfileLink> getProfileLinksOf(Seller seller);

    ProfileLink createLinkOf(Long sellerId, ProfileLinkRequestDTO request);

    ProfileLink updateLinkOf(Long sellerId, ProfileLinkRequestDTO request);

    Page<ProfileLink> getLinkListOf(Long sellerId);

    void deleteLink(Long linkId);
}
