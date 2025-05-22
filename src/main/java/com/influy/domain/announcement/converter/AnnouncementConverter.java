package com.influy.domain.announcement.converter;

import com.influy.domain.announcement.dto.AnnouncementRequestDTO;
import com.influy.domain.announcement.dto.AnnouncementResponseDTO;
import com.influy.domain.announcement.entity.Announcement;
import com.influy.domain.seller.entity.Seller;

public class AnnouncementConverter {
    public static Announcement toEntity(AnnouncementRequestDTO requestDTO, Seller seller){
        return Announcement.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .seller(seller)
                .build();
    }

    public static AnnouncementResponseDTO.General toGeneralDTO(Announcement announcement) {

        return AnnouncementResponseDTO.General.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .isPrimary(announcement.getIsPrimary())
                .build();
    }
}
