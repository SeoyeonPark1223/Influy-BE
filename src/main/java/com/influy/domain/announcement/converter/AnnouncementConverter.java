package com.influy.domain.announcement.converter;

import com.influy.domain.announcement.dto.AnnouncementRequestDTO;
import com.influy.domain.announcement.dto.AnnouncementResponseDTO;
import com.influy.domain.announcement.entity.Announcement;
import com.influy.domain.seller.entity.Seller;
import org.springframework.data.domain.Page;

public class AnnouncementConverter {
    public static Announcement toEntity(AnnouncementRequestDTO requestDTO, Seller seller){
        return Announcement.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .seller(seller)
                .isPrimary(requestDTO.getIsPrimary())
                .build();
    }

    public static AnnouncementResponseDTO.General toGeneralDTO(Announcement announcement) {

        return AnnouncementResponseDTO.General.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .isPrimary(announcement.getIsPrimary())
                .createdAt(announcement.getCreatedAt())
                .build();
    }

    public static AnnouncementResponseDTO.GeneralList toListDTO(Page<Announcement> announcements) {

        return AnnouncementResponseDTO.GeneralList.builder()
                .announcements(announcements.getContent().stream().map(AnnouncementConverter::toGeneralDTO).toList())
                .totalPage(announcements.getTotalPages())
                .totalElements(announcements.getTotalElements())
                .listSize(announcements.getSize())
                .isFirst(announcements.isFirst())
                .isLast(announcements.isLast())
                .build();

    }
}
