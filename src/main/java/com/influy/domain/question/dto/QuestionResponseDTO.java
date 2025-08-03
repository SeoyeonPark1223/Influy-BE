package com.influy.domain.question.dto;

import com.influy.domain.answer.dto.AnswerResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionResponseDTO {
    public interface UserViewQNA {}

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserViewQNAPage{
        private List<UserViewQNA> chatList;
        @Schema(description = "이 페이지의 리스트 사이즈", example = "20")
        private Integer listSize;
        @Schema(description = "총 페이지", example = "13")
        private Integer totalPage;
        @Schema(description = "전체 채팅 개수", example = "260")
        private Long totalElements;
        @Schema(description = "지금 첫 페이지인지", example = "false")
        private Boolean isFirst;
        @Schema(description = "마지막 페이지인지", example = "false")
        private Boolean isLast;

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserViewQuestion implements UserViewQNA{
        @Schema(description = "타입", example = "Q")
        private String type;
        @Schema(description = "질문 아이디", example = "1")
        private Long id;
        @Schema(description = "질문이 속한 카테고리 이름", example = "색상")
        private String categoryName;
        @Schema(description = "내용", example = "더 싸게는 안되나요?")
        private String content;
        @Schema(description = "생성 일자", example = "2025-01-03Z13:13:13")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SellerViewQuestion {
        @Schema(description = "질문 아이디", example = "1")
        private Long questionId;
        @Schema(description = "질문한 회원 아이디", example = "2")
        private Long memberId;
        @Schema(description = "질문한 회원 프사", example = "https://amazon.~")
        private String profileImg;
        @Schema(description = "질문한 회원 닉네임", example = "당기당기당기누")
        private String nickname;
        @Schema(description = "질문한 회원 유저네임", example = "@pullpullpull")
        private String username;
        @Schema(description = "질문 태그 이름")
        private String tagName;
        @Schema(description = "질문 태그 아이디")
        private Long tagId;
        @Schema(description = "새로 들어온 질문인지(빨간점)", example = "true")
        private boolean isNew;
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
    public static class SellerViewPage {
        private List<SellerViewQuestion> questions;
        @Schema(description = "이 페이지의 리스트 사이즈", example = "20")
        private Integer listSize;
        @Schema(description = "미확인 질문 개수", example = "3")
        private Long newQuestionCnt;
        @Schema(description = "총 페이지", example = "13")
        private Integer totalPage;
        @Schema(description = "전체 질문 개수", example = "260")
        private Long totalElements;
        @Schema(description = "지금 첫 페이지인지", example = "false")
        private Boolean isFirst;
        @Schema(description = "마지막 페이지인지", example = "false")
        private Boolean isLast;
    }

    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreationResult {

        @Schema(description = "질문 아이디", example = "1")
        private Long id;
        @Schema(description = "내용", example = "더 싸게는 안되나요?")
        private String content;
        @Schema(description = "질문 대분류 카테고리 이름", example = "색상")
        private String categoryName;
        @Schema(description = "생성 일자", example = "2025-01-03Z13:13:13")
        private LocalDateTime createdAt;
    }

    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IsAnsweredCntDTO {
        @Schema(description = "답변 대기 개수", example = "14")
        private Long waitingCnt;
        @Schema(description = "답변 완료 개수", example = "30")
        private Long completedCnt;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionViewDto {
        @Schema(description = "질문 아이디", example = "1")
        private Long questionId;

        @Schema(description = "질문한 회원 아이디", example = "2")
        private Long memberId;

        @Schema(description = "질문한 회원 유저네임", example = "@pullpullpull")
        private String username;

        @Schema(description = "내용", example = "더 싸게는 안되나요?")
        private String content;

        @Schema(description = "몇차 질문", example = "4")
        private Long nthQuestion;

        @Schema(description = "질문 시간", example = "2025-01-03Z13:13:13")
        private LocalDateTime createdAt;

        @Schema(description = "질문 태그 이름", example = "네이비")
        private String tagName;

        @Schema(description = "질문 태그 id", example = "1")
        private Long tagId;

        @Schema(description = "유저 프로필 이미지", example = "1")
        private String profileImg;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QnAListDto {
        @Schema(description = "질문 뷰")
        private QuestionViewDto questionDto;

        @Schema(description = "답변 리스트 뷰")
        private AnswerResponseDto.AnswerViewListDto answerListDto;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteResultDto {
        @Schema(description = "삭제된 질문 id 리스트")
        private List<Long> questionIdList;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserTalkBoxItemDTO {
        @Schema(description = "해당 톡박스 아이템 id", example="1")
        private Long itemId;
        @Schema(description = "해당 톡박스 아이템 이름", example="뿌링뿌링클")
        private String itemTitle;
        @Schema(description = "해당 톡박스 아이템 대표 사진", example="http:/amazon.s3~")
        private String itemMainPic;
        @Schema(description = "아이템 셀러 닉네임", example="소현소현")
        private String sellerNickname;
        @Schema(description = "셀러 프로필 사진", example="http://amazon.s3~")
        private String sellerProfilePic;
        @Schema(description = "가장 최근 대화 내역(답변/질문 구분 X)", example="환불 도와드리겠습니다")
        private String lastChatContent;
        @Schema(description = "가장 최근 채팅 시간", example="2025-02-04Z23:11:11")
        private LocalDateTime lastChatTime;
        @Schema(description = "확인하지 않은 대화 개수", example="2")
        private Integer uncheckedCnt;
    }

    @Getter @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access=AccessLevel.PROTECTED)
    public static class UserTalkBoxItemPageDTO{
        private List<UserTalkBoxItemDTO> talkboxList;
        @Schema(description = "리스트 사이즈", example = "10")
        private Integer listSize;
        @Schema(description = "총 페이지", example = "2")
        private Integer totalPage;
        @Schema(description = "전체 톡박스 개수", example = "30")
        private Long totalElements;
        @Schema(description = "지금 첫 페이지인지", example = "false")
        private Boolean isFirst;
        @Schema(description = "마지막 페이지인지", example = "false")
        private Boolean isLast;
    }
}
