package com.influy.domain.announcement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "공지 아이디", example = "1")
        private Long id;
        @Schema(description = "공지 제목", example = "긴급 공지합니다!!")
        private String title;
        @Schema(description = "공지 내용", example = "폐업합니다")
        private String content;
        @Schema(description = "최상단 고정 여부", example = "true")
        private Boolean isPrimary;
        @Schema(description = "생성 일자", example = "2025-07-01Z13:05:11")
        private LocalDateTime createdAt;
    }

    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeneralList{
        private List<General> announcements;
        @Schema(description = "리스트 크기", example = "20")
        private Integer listSize;
        @Schema(description = "전체 페이지", example = "12")
        private Integer totalPage;
        @Schema(description = "전체 요소 개수", example = "2")
        private Long totalElements;
        @Schema(description = "현재 페이지가 첫번째 페이지인지", example = "true")
        private Boolean isFirst;
        @Schema(description = "현재 페이지가 마지막 페이지인지", example = "false")
        private Boolean isLast;
    }

    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PinnedAnnouncement{
        @Schema(description = "공지 아이디", example = "1")
        private Long id;
        @Schema(description = "최상단 공지 제목", example = "긴급 공지합니다!!")
        private String title;
        @Schema(description = "전체 공지 개수", example = "7")
        private Integer totalAnnouncements;
    }
}
