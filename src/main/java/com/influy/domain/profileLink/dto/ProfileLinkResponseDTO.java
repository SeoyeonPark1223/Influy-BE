package com.influy.domain.profileLink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProfileLinkResponseDTO {

    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class General{
        @Schema(description = "링크 아이디", example = "1")
        private Long id;
        @Schema(description = "링크 이름", example = "네이버 스토어")
        private String linkName;
        @Schema(description = "링크", example = "https://~ 서버단 제약 X")
        private String link;
    }
}
