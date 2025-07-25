package com.influy.domain.answer.dto;

import com.influy.domain.answer.entity.AnswerType;
import com.influy.domain.item.entity.TalkBoxOpenStatus;
import com.influy.domain.question.dto.QuestionResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class AnswerResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerTagListDto {
        @Schema(description = "태그 이름", example = "네이비")
        private String tag;

        @Schema(description = "답변 리스트")
        private List<String> answerList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserViewAnswer implements QuestionResponseDTO.UserViewQNA {
        @Schema(description = "유형", example = "Q")
        private String type;
        @Schema(description = "답변 아이디", example = "1")
        private Long id;
        @Schema(description = "답변한 질문 아이디", example = "3")
        private Long questionId;
        @Schema(description = "답변 타입", example = "일괄 답변")
        private String answerType;
        @Schema(description = "답변한 질문 내용", example = "더 싸게는 안되나요?")
        private String questionContent;
        @Schema(description = "내용", example = "네 그건 좀 어렵습니다 ㅜㅜ")
        private String content;
        @Schema(description = "생성 일자", example = "2025-01-03Z13:13:13")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerCommonResultDto {
        @Schema(description = "답변한 질문 개수", example = "4")
        private Integer answeredCnt;

        @Schema(description = "답변 관련 정보 리스트")
        private List<AnswerResultDto> answerDtoList;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerResultDto {
        @Schema(description = "답변한 질문 id", example = "1")
        private Long questionId;

        @Schema(description = "답변 id", example = "1")
        private Long answerId;

        @Schema(description = "답변 타입 [COMMON, INDIVIDUAL, FAQ", example = "INDIVIDUAL")
        private AnswerType answerType;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerViewListDto {
        @Schema(description = "답변 리스트")
        private List<AnswerViewDto> answerViewList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerViewDto {
        @Schema(description = "답변 id", example = "1")
        private Long answerId;

        @Schema(description = "답변 타입 [COMMON, INDIVIDUAL, FAQ", example = "INDIVIDUAL")
        private AnswerType answerType;

        @Schema(description = "답변 내용", example = "울랄라 이건 이거에요")
        private String answerContent;

        @Schema(description = "답변 생성일", example = "021-01-01T00:00")
        private LocalDateTime answerTime;
    }
}
