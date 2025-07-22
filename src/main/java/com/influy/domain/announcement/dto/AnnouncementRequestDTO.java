package com.influy.domain.announcement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AnnouncementRequestDTO {
    @Schema(description = "공지 제목", example = "긴급 공지합니다!!")
    private String title;
    @Schema(description = "공지 내용", example = "폐업합니다")
    private String content;
}
