package com.influy.domain.profileLink.service;

import com.influy.domain.profileLink.dto.ProfileLinkRequestDTO;
import com.influy.domain.profileLink.entity.ProfileLink;

import java.util.List;

public interface ProfileLinkService {

    ProfileLink createLinkOf(Long sellerId, ProfileLinkRequestDTO request);

    ProfileLink updateLinkOf(Long sellerId, Long linkId, ProfileLinkRequestDTO request);

    List<ProfileLink> getLinkListOf(Long sellerId);

    void deleteLinkOf(Long sellerId, Long linkId);
}
