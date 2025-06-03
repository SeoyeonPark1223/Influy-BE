package com.influy.domain.item.dto;

import com.influy.domain.item.entity.ItemStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailDto {
        @Schema(description = "아이템 사진 리스트, 대표사진은 리스트 맨 처음 순서로", example = "[\"xxx.png\", \"xxxxx.png\", \"xxxxxx.png\"]")
        @Size(max = 10, message = "이미지는 최대 10개까지만 업로드할 수 있습니다.")
        private List<String> itemImgList;

        @Schema(description = "아이템 제목", example = "제작 원피스")
        private String name;

        @Schema(description = "아이템 카테고리, 1~3개", example = "[\"뷰티\", \"패션\"]")
        @Size(max = 3, message = "카테고리는 최대 3개까지만 업로드할 수 있습니다.")
        private List<String> itemCategoryList;

        @Schema(description = "시작일", example = "021-01-01T00:00")
        private LocalDateTime startDate;

        @Schema(description = "마감일", example = "021-01-01T00:00")
        private LocalDateTime endDate;

        @Schema(description = "한줄 소개", example = "빤짝거리는 원피스입니다")
        private String tagline;

        @Schema(description = "정가", example = "100000")
        private Long regularPrice;

        @Schema(description = "할인가", example = "80000")
        private Long salePrice;

        @Schema(description = "판매 링크", example = "xxxx.com")
        private String marketLink;

        @Schema(description = "진행 회차", example = "1")
        private Integer itemPeriod;

        @Schema(description = "코멘트", example = "이렇게 빤짝이는 드레스 흔하지 않아요 어렵게 구해왔어요")
        private String comment;

        @Schema(description = "보관 여부 (보관: true, 게시: false)", example = "false")
        private Boolean isArchived;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccessDto {
        @Schema(description = "홈아카이브 추천 허용", example = "false")
        private Boolean archiveRecommended;

        @Schema(description = "서비스 내 검색 허용", example = "false")
        private Boolean searchAvailable;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusDto {
        @Schema(description = "아이템 표기 상태", example = "SOLD_OUT")
        private ItemStatus status;
    }
}
