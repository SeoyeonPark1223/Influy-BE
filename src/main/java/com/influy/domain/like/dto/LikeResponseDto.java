package com.influy.domain.like.dto;

import com.influy.domain.item.dto.ItemResponseDto;
import com.influy.domain.item.entity.ItemStatus;
import com.influy.domain.like.entity.LikeStatus;
import com.influy.domain.like.entity.TargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class LikeResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellerLikeDto {
        @Schema(description = "찜 id", example = "1")
        private Long likeId;

        @Schema(description = "멤버 id", example = "1")
        private Long memberId;

        @Schema(description = "셀러/아이템 찜", example = "SELLER")
        private TargetType targetType;

        @Schema(description = "LIKE/DISLIKE 여부", example = "LIKE")
        private LikeStatus likeStatus;

        @Schema(description = "셀러 id", example = "1")
        private Long sellerId;

        @Schema(description = "셀러 닉네임", example = "뾰로롱")
        private String sellerName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemLikeDto {
        @Schema(description = "찜 id", example = "1")
        private Long likeId;

        @Schema(description = "멤버 id", example = "1")
        private Long memberId;

        @Schema(description = "셀러/아이템 찜", example = "ITEM")
        private TargetType targetType;

        @Schema(description = "LIKE/DISLIKE 여부", example = "LIKE")
        private LikeStatus likeStatus;

        @Schema(description = "아이템 id", example = "1")
        private Long itemId;

        @Schema(description = "아이템 이름", example = "빤짝 원피스")
        private String itemName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikeCountDto {
        @Schema(description = "셀러/아이템 찜", example = "ITEM")
        private TargetType targetType;

        @Schema(description = "셀러/아이템 id", example = "1")
        private Long targetId;

        @Schema(description = "찜 개수", example = "10")
        private Integer likeCount;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewSellerLikeDto {
        @Schema(description = "셀러 id", example = "1")
        private Long sellerId;

        @Schema(description = "셀러 닉네임", example = "샤방샤방")
        private String nickName;

        @Schema(description = "셀러 아이디", example = "iamseller")
        private String userName;

        @Schema(description = "프로필 사진", example = "https://influy-s3...")
        private String profileImgLink;

        @Schema(description = "셀러 좋아요 개수", example = "23")
        private Long likeCnt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewItemLikeDto {
        @Schema(description = "아이템 id", example = "1")
        private Long itemId;

        @Schema(description = "셀러 id", example = "1")
        private Long sellerId;

        @Schema(description = "대표 사진", example = "xxxx.png")
        private String mainImg;

        @Schema(description = "진행 차수", example = "1")
        private Integer itemPeriod;

        @Schema(description = "아이템 이름", example = "원피스")
        private String itemName;

        @Schema(description = "셀러 이름", example = "@thgusthgus")
        private String sellerName;

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
    public static class SellerLikePageDto {
        @Schema(description = "찜한 셀러 리스트")
        private List<ViewSellerLikeDto> sellerLikeList;

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
    public static class ItemLikePageDto {
        @Schema(description = "찜한 아이템 리스트")
        private List<ViewItemLikeDto> itemLikeList;

        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }
}
