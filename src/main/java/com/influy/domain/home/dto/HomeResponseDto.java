package com.influy.domain.home.dto;

import com.influy.domain.item.entity.ItemStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class HomeResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HomeItemViewDto {
        @Schema(description = "셀러 프로필사진", example = "https://...")
        private String sellerProfileImg;

        @Schema(description = "셀러 아이디", example = "@xoyeon")
        private String sellerUsername;

        @Schema(description = "셀러 닉네임", example = "소현소현")
        private String sellerNickname;

        @Schema(description = "셀러 id", example = "1")
        private Long sellerId;

        @Schema(description = "아이템 id", example = "1")
        private Long itemId;

        @Schema(description = "아이템 대표 사진", example = "https://...")
        private String itemMainImg;

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

        @Schema(description = "아이템 상태 [ DEFAULT, EXTEND, SOLD_OUT ]", example = "DEFAULT")
        private ItemStatus currentStatus;

        @Schema(description = "찜 여부", example = "false")
        private Boolean liked;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HomeItemViewPageDto {
        @Schema(description = "아이템 홈 뷰 리스트")
        private List<HomeItemViewDto> itemPreviewList;

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
    public static class SellerThumbnailListDto {
        @Schema(description = "셀러 프로필 썸네일 리스트 (10개)")
        private List<SellerThumbnailDto> sellerThumbnailList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellerThumbnailDto {
        @Schema(description = "셀러 id", example = "1")
        private Long sellerId;

        @Schema(description = "셀러 프로필 이미지", example = "https://..")
        private String profileImg;

        @Schema(description = "셀러 아이디", example = "thgusth")
        private String sellerUsername;

        @Schema(description = "셀러 닉네임", example = "혜선")
        private String sellerNickname;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellerPick3Dto {
        @Schema(description = "셀러 id", example = "1")
        private Long sellerId;

        @Schema(description = "셀러 닉네임", example = "혜선")
        private String sellerNickname;

        @Schema(description = "상품 3개 대표 이미지 리스트")
        private List<String> mainImgList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellerHomeItemPageDTO {
        @Schema(description = "아이템 preview 리스트")
        private List<SellerHomeItemDTO> itemList;

        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SellerHomeItemDTO {
        @Schema(description = "아이템 id", example = "1")
        private Long itemId;
        @Schema(description = "아이템 사진",example = "https://amazon~")
        private String imageUrl;
        @Schema(description = "아이템 상태", example = "DEFAULT")
        private ItemStatus itemStatus;
        @Schema(description = "아이템 진행 차수",example = "2")
        private Integer itemPeriod;
        @Schema(description = "상품 이름",example = "신나는 여행 패키지")
        private String itemTitle;
        @Schema(description = "시작 시간",example = "2025-05-20Z18:29:44")
        private LocalDateTime startDate;
        @Schema(description = "마감 시간",example = "2025-09-20Z18:29:44")
        private LocalDateTime endDate;
        @Schema(description = "전체 질문(질문 대기)",example = "12")
        private Long totalPendingQuestions;
        @Schema(description = "확인하지 않은 질문 수",example = "3")
        private Long newQuestions;
        @Schema(description = "질문이 가장 많이 들어오는 3개 카테고리",example = "[\"일자 조정\",\"인원 수 문의\"]")
        private List<String> topCategories;
    }
}
