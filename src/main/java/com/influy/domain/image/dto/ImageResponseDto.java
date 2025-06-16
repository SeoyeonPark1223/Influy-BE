package com.influy.domain.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ImageResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploadResultDto {
        @Schema(description = "정상적으로 업로드되면 활성화될 imageURL", example = "https://influy-s3...")
        private String imgUrl;
    }
}
