package com.influy.domain.profileLink.converter;

import com.influy.domain.profileLink.dto.ProfileLinkResponseDTO;
import com.influy.domain.profileLink.entity.ProfileLink;

public class ProfileLinkConverter {

    public static ProfileLinkResponseDTO.General toGeneralDTO(ProfileLink profileLink) {
        return ProfileLinkResponseDTO.General.builder()
                .linkName(profileLink.getLinkName())
                .link(profileLink.getLink())
                .build();
    }
    
}
