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

    // 셀러 에러 응답
    SELLER_NOT_FOUND(HttpStatus.NOT_FOUND, "SELLER NOT FOUND", "셀러를 찾을 수 없습니다."),

    // 카테고리 관련 응답
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY NOT FOUND", "카테고리를 찾을 수 없습니다."),

    // 아이템 관련 응답
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "ITEM NOT FOUND", "아이템을 찾을 수 없습니다."),

    // 정렬 관련 응답
    UNSUPPORTED_SORT_TYPE(HttpStatus.BAD_REQUEST, "UNSUPPORTED SORT TYPE", "가능한 정렬 방식은 CREATE_DATE, END_DATE 입니다.");


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
