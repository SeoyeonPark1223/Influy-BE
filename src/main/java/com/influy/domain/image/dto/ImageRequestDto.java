package com.influy.domain.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ImageRequestDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploadDto {
        @Schema(description = "확장자를 포함한 이미지 이름", example = "influy.png")
        private String imgName;
    }
}
