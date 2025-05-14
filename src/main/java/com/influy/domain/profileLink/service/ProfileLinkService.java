package com.influy.domain.profileLink.service;

import com.influy.domain.profileLink.entity.ProfileLink;
import com.influy.domain.profileLink.entity.ProfileLinkType;
import com.influy.domain.profileLink.repository.ProfileLinkRepository;
import com.influy.domain.seller.entity.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileLinkService {

    private final ProfileLinkRepository profileLinkRepository;

    public List<ProfileLink> getProfileLinksByType(Seller seller, ProfileLinkType type) {
        return profileLinkRepository.findBySellerAndLinkType(seller,type);
    }

    public List<ProfileLink> getProfileLinksOf(Seller seller) {
        return profileLinkRepository.findBySeller(seller);
    }
}
