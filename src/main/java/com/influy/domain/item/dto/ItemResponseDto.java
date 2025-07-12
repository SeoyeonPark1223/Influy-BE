package com.influy.domain.item.dto;

import com.influy.domain.item.entity.ItemStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ItemResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResultDto {
        @Schema(description = "아이템 id", example = "1")
        private Long itemId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailPreviewDto {
        @Schema(description = "아이템 id", example = "1")
        private Long itemId;

        @Schema(description = "대표 사진", example = "xxxx.png")
        private String MainImg;

        @Schema(description = "진행 차수", example = "1")
        private Integer itemPeriod;

        @Schema(description = "아이템 이름", example = "원피스")
        private String itemName;

        @Schema(description = "셀러 이름", example = "소현소현")
        private String sellerName;

        @Schema(description = "시작일", example = "021-01-01T00:00")
        private LocalDateTime startDate;

        @Schema(description = "마감일", example = "021-01-01T00:00")
        private LocalDateTime endDate;

        @Schema(description = "한줄 소개", example = "빤짝거리는 원피스입니다")
        private String tagline;

        @Schema(description = "아이템 상태", example = "DEFAULT")
        private ItemStatus currentStatus;

        @Schema(description = "찜 여부", example = "false")
        private Boolean liked;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailPreviewPageDto {
        @Schema(description = "아이템 preview 리스트")
        private List<DetailPreviewDto> itemPreviewList;

        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailViewDto {
        @Schema(description = "아이템 id", example = "1")
        private Long itemId;

        @Schema(description = "진행 차수", example = "1")
        private Integer itemPeriod;

        @Schema(description = "아이템 이름", example = "원피스")
        private String itemName;

        @Schema(description = "시작일", example = "021-01-01T00:00")
        private LocalDateTime startDate;

        @Schema(description = "마감일", example = "021-01-01T00:00")
        private LocalDateTime endDate;

        @Schema(description = "한줄 소개", example = "빤짝거리는 원피스입니다")
        private String tagline;

        @Schema(description = "아이템 상태", example = "DEFAULT")
        private ItemStatus currentStatus;

        @Schema(description = "판매 링크", example = "xxxx.com")
        private String marketLink;

        @Schema(description = "보관 여부 (보관: true, 게시: false)", example = "false")
        private Boolean isArchived;

        @Schema(description = "아이템 사진 리스트, 대표사진은 리스트 맨 처음 순서로", example = "[xxx.png, xxxxx.png, xxxxxx.png]")
        private List<String> itemImgList;

        @Schema(description = "아이템 카테고리, 1~3개", example = "[뷰티, 패션, 소품]")
        private List<String> itemCategoryList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountDto {
        @Schema(description = "셀러 id", example = "1")
        private Long sellerId;

        @Schema(description = "공개/보관 아이템 개수", example = "5")
        private Integer count;
    }
}
