package com.influy.domain.profileLink.service;

import com.influy.domain.profileLink.dto.ProfileLinkRequestDTO;
import com.influy.domain.profileLink.entity.ProfileLink;
import com.influy.domain.profileLink.repository.ProfileLinkRepository;
import com.influy.domain.seller.entity.Seller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileLinkServiceImpl implements ProfileLinkService {

    private final ProfileLinkRepository profileLinkRepository;

    @Override
    public List<ProfileLink> getProfileLinksOf(Seller seller) {

        return profileLinkRepository.findBySeller(seller);
    }

    @Override
    public void createLinkOf(Long sellerId, ProfileLinkRequestDTO request) {

    }
}
