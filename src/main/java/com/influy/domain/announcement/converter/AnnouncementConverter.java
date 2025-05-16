package com.influy.domain.announcement.converter;

import com.influy.domain.announcement.dto.AnnouncementResponseDTO;
import com.influy.domain.announcement.entity.Announcement;

public class AnnouncementConverter {
    public static AnnouncementResponseDTO.General toGeneralDTO(Announcement announcement) {

        return AnnouncementResponseDTO.General.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .isPrimary(announcement.getIsPrimary())
                .build();
    }
}
