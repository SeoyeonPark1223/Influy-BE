package com.influy.domain.profileLink.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ProfileLinkRequestDTO {
    @Schema(description = "링크 이름", example = "네이버 스토어")
    private String linkName;
    @Schema(description = "링크 값", example = "https://~ 서버단 제약 X")
    private String link;
}
