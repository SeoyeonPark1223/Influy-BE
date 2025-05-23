package com.influy.domain.announcement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AnnouncementResponseDTO {

    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class General{
        private Long id;
        private String title;
        private String content;
        private Boolean isPrimary;
        private LocalDateTime createdAt;
    }
}
