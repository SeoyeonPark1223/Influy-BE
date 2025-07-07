package com.influy.domain.profileLink.converter;

import com.influy.domain.profileLink.dto.ProfileLinkRequestDTO;
import com.influy.domain.profileLink.dto.ProfileLinkResponseDTO;
import com.influy.domain.profileLink.entity.ProfileLink;
import com.influy.domain.sellerProfile.entity.SellerProfile;

public class ProfileLinkConverter {

    public static ProfileLink toEntity(ProfileLinkRequestDTO requestDTO, SellerProfile seller) {

        return ProfileLink.builder()
                .seller(seller)
                .link(requestDTO.getLink())
                .linkName(requestDTO.getLinkName())
                .build();
    }

    public static ProfileLinkResponseDTO.General toGeneralDTO(ProfileLink profileLink) {
        return ProfileLinkResponseDTO.General.builder()
                .id(profileLink.getId())
                .linkName(profileLink.getLinkName())
                .link(profileLink.getLink())
                .build();
    }
    
}
