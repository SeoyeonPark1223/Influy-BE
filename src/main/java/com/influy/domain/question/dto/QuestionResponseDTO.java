package com.influy.domain.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionResponseDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class General {
        @Schema(description = "질문 아이디", example = "1")
        private Long id;
        @Schema(description = "질문한 회원 아이디", example = "2")
        private Long memberId;
        @Schema(description = "질문한 회원 닉네임", example = "당기당기당기누")
        private String nickname;
        @Schema(description = "질문한 회원 유저네임", example = "@pullpullpull")
        private String username;
        @Schema(description = "내용", example = "더 싸게는 안되나요?")
        private String content;
        @Schema(description = "해당 셀러에게 얼마나 질문했는지", example = "4")
        private Long nthQuestion;
        @Schema(description = "생성 일자", example = "2025-01-03Z13:13:13")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeneralPage {
        private List<General> questions;
        @Schema(description = "이 페이지의 리스트 사이즈", example = "20")
        private Integer listSize;
        @Schema(description = "총 페이지", example = "13")
        private Integer totalPage;
        @Schema(description = "전체 개수", example = "260")
        private Long totalElements;
        @Schema(description = "지금 첫 페이지인지", example = "false")
        private Boolean isFirst;
        @Schema(description = "마지막 페이지인지", example = "false")
        private Boolean isLast;
    }
}
