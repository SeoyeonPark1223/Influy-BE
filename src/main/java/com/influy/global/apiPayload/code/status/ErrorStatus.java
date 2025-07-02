package com.influy.global.apiPayload.code.status;

import com.influy.global.apiPayload.code.BaseCode;
import com.influy.global.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {

    // 기본 에러 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    
    //공지
    ANNOUNCEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "ANNOUNCEMENT401", "요청하신 공지를 찾을 수 없습니다."),

    // 멤버 관련 응답
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER NOT FOUND", "멤버를 찾을 수 없습니다."),

    // 찜 관련 응답
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "LIKE NOT FOUND", "찜을 찾을 수 없습니다."),

    // 셀러 에러 응답
    SELLER_NOT_FOUND(HttpStatus.NOT_FOUND, "SELLER NOT FOUND", "셀러를 찾을 수 없습니다."),
    
    //링크 인 바이오 관련 응답
    LINK_NOT_FOUND(HttpStatus.NOT_FOUND, "LINK NOT FOUND", "링크를 찾을 수 없습니다."),
    LINK_COUNT_LIMIT(HttpStatus.FORBIDDEN, "LINK COUNT LIMIT", "링크 최대 등록 개수를 초과합니다."),

    // 카테고리 관련 응답
    ITEM_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "ITEM CATEGORY NOT FOUND", "아이템 카테고리를 찾을 수 없습니다."),
    FAQ_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "FAQ CATEGORY NOT FOUND", "FAQ 카테고리를 찾을 수 없습니다."),
    INVALID_FAQ_ITEM_RELATION(HttpStatus.BAD_REQUEST, "INVALID FAQ ITEM RELATION", "해당 FAQ 카테고리의 아이템을 찾을 수 없습니다."),
    FAQ_CATEGORY_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "FAQ CATEGORY ALREADY EXISTS", "해당 FAQ 카테고리가 이미 존재합니다."),

    // FAQ 카드 관련 응답
    FAQ_CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "FAQ CARD NOT FOUND", "FAQ 카드를 찾을 수 없습니다."),

    // 아이템 관련 응답
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "ITEM NOT FOUND", "아이템을 찾을 수 없습니다."),
    UNMATCHED_SELLER_ITEM(HttpStatus.BAD_REQUEST, "UNMATCHED SELLER ITEM", "셀러가 해당 아이템을 가지고 있지 않습니다."),
    UNMATCHED_ITEM_FAQCATEGORY(HttpStatus.BAD_REQUEST, "UNMATCHED ITEM FAQCATEGORY", "아이템이 해당 FAQ 카테고리를 가지고 있지 않습니다."),

    //질문관리창 관련 응당
    QUESTION_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION CATEGORY NOT FOUND", "질문 카테고리를 찾을 수 없습니다."),

    // 정렬 관련 응답
    UNSUPPORTED_SORT_TYPE(HttpStatus.BAD_REQUEST, "UNSUPPORTED SORT TYPE", "가능한 정렬 방식이 아닙니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
