package com.influy.domain.announcement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeneralList{
        private List<General> announcements;
        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }
}
