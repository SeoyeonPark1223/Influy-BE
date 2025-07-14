package com.influy.global.apiPayload.code.status;

import com.influy.global.apiPayload.code.BaseCode;
import com.influy.global.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    // 기본 성공 응답
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),

    //삭제 성공
    DELETE_SUCCESS(HttpStatus.OK, "DELETE SUCCESS", "삭제가 완료되었습니다."),

    //탈퇴 성공
    ACCOUNT_DELETE_SUCCESS(HttpStatus.OK, "ACCOUNT DELETE SUCCESS", "탈퇴가 완료되었습니다."),

    //사용 가능한 리소스
    NO_DUPLICATE_ROW(HttpStatus.OK, "NO DUPLICATE ROW", "중복되는 인스턴스가 없습니다."),

    //로그아웃 성공
    LOGOUT_SUCCESS(HttpStatus.OK, "LOGOUT SUCCESS", "로그아웃이 완료되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}

