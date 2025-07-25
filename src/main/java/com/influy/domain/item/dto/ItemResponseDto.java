package com.influy.domain.item.dto;

import com.influy.domain.item.entity.ItemStatus;
import com.influy.domain.item.entity.TalkBoxOpenStatus;
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

        @Schema(description = "셀러 id", example = "1")
        private Long sellerId;

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

        @Schema(description = "셀러일 경우 톡박스 관련 정보")
        private TalkBoxInfoDto talkBoxInfo;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TalkBoxInfoDto {
        @Schema(description = "톡박스 오픈 상태 [INITIAL, OPENED, CLOSED]", example = "INITIAL")
        private TalkBoxOpenStatus talkBoxOpenStatus;

        @Schema(description = "응답 대기 개수", example = "123")
        private Integer waitingCnt;

        @Schema(description = "응답 완료 개수", example = "123")
        private Integer completedCnt;
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

        @Schema(description = "정가", example = "100000")
        private Long regularPrice;

        @Schema(description = "할인가", example = "80000")
        private Long salePrice;

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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemOverviewDto {
        @Schema(description = "아이템 id", example = "1")
        private Long id;

        @Schema(description = "아이템 이름", example = "원피스")
        private String itemName;

        @Schema(description = "한줄 소개", example = "빤짝거리는 원피스입니다")
        private String tagline;

        @Schema(description = "대표 사진", example = "xxxx.png")
        private String mainImg;

        @Schema(description = "해당 아이템 톡박스 오픈 여부", example = "OPEN")
        private TalkBoxOpenStatus talkBoxOpenStatus;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TalkBoxOpenStatusDto {
        @Schema(description = "아이템 id", example = "1")
        private Long itemId;

        @Schema(description = "해당 톡박스 상태 [INITIAL, OPENED, CLOSED]", example = "OPENED")
        private TalkBoxOpenStatus status;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewTalkBoxCommentDto {
        @Schema(description = "셀러 id", example = "1")
        private Long sellerId;

        @Schema(description = "셀러 프로필사진", example = "https://...")
        private String sellerProfileImg;

        @Schema(description = "셀러 아이디", example = "xoyeon")
        private String sellerUsername;

        @Schema(description = "셀러 닉네임", example = "소현")
        private String sellerNickname;

        @Schema(description = "생성일", example = "021-01-01T00:00")
        private LocalDateTime createdAt;

        @Schema(description = "톡박스 기본 멘트", example = "울랄라 먐마미아")
        private String talkBoxComment;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TalkBoxOpenedListDto {
        @Schema(description = "톡박스가 활성화된 아이템 리스트")
        private List<TalkBoxOpenedDto> talkBoxOpenedDtoList;

        @Schema(description = "톡박스가 활성화된 아이템 개수", example = "4")
        private Integer cnt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TalkBoxOpenedDto {
        @Schema(description = "아이템 id", example = "1")
        private Long itemId;

        @Schema(description = "아이템 대표사진", example = "https:/....")
        private String itemMainImg;

        @Schema(description = "아이템 이름", example = "컬러 리들샷")
        private String itemName;

        @Schema(description = "응답대기(waitingCnt) / 응답완료(completedCnt) 개수")
        private TalkBoxInfoDto talkBoxCntInfo;

        @Schema(description = "해당 상품에 새로 들어온 질문 총개수", example = "5")
        private Integer newCnt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HomeItemViewDto {
        @Schema(description = "셀러 프로필사진", example = "https://...")
        private String sellerProfileImg;

        @Schema(description = "셀러 아이디", example = "@xoyeon")
        private String sellerUsername;

        @Schema(description = "아이템 id", example = "1")
        private Long itemId;

        @Schema(description = "아이템 대표 사진", example = "https://...")
        private String itemMainImg;

        @Schema(description = "진행 차수", example = "1")
        private Integer itemPeriod;

        @Schema(description = "아이템 이름", example = "원피스")
        private String itemName;

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
}
