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

    /**
     * 멤버 관련 응답
     */
    //1. 전역 상황
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER NOT FOUND", "멤버를 찾을 수 없습니다."),

    //2. 회원가입/프로필 수정
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "MEMBER ALREADY EXIST", "이미 존재하는 멤버입니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "USERNAME ALREADY EXISTS", "중복된 아이디입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "EMAIL ALREADY EXISTS", "중복된 이메일입니다."),
    YOUYUBE_ALREADY_EXISTS(HttpStatus.CONFLICT, "EMAIL ALREADY EXISTS", "중복된 유튜브 계정입니다."),
    TIKTOK_ALREADY_EXISTS(HttpStatus.CONFLICT, "EMAIL ALREADY EXISTS", "중복된 틱톡 계정입니다."),
    INSTAGRAM_ALREADY_EXISTS(HttpStatus.CONFLICT, "EMAIL ALREADY EXISTS", "중복된 인스타 계정입니다."),
    REQUIRES_INTERESTED_CATEGORY(HttpStatus.BAD_REQUEST, "REQUIRES INTERESTED CATEGORY", "관심 카테고리 리스트는 필수입니다"),
    LOGIN_REQUIRED(HttpStatus.BAD_REQUEST, "LOGIN REQUIRED", "로그인이 필요한 서비스입니다."),


    //공지
    ANNOUNCEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "ANNOUNCEMENT NOT FOUND", "요청하신 공지를 찾을 수 없습니다."),


    // 찜 관련 응답
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "LIKE NOT FOUND", "찜을 찾을 수 없습니다."),

    // 셀러 에러 응답
    SELLER_NOT_FOUND(HttpStatus.NOT_FOUND, "SELLER NOT FOUND", "셀러를 찾을 수 없습니다."),
    SELLER_REQUIRED(HttpStatus.BAD_REQUEST, "SELLER REQUIRED", "셀러만 사용할 수 있습니다."),
    
    //링크 인 바이오 관련 응답
    LINK_NOT_FOUND(HttpStatus.NOT_FOUND, "LINK NOT FOUND", "링크를 찾을 수 없습니다."),
    LINK_COUNT_LIMIT(HttpStatus.FORBIDDEN, "LINK COUNT LIMIT", "링크 최대 등록 개수를 초과합니다."),

    // 카테고리 관련 응답
    ITEM_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "ITEM CATEGORY NOT FOUND", "아이템 카테고리를 찾을 수 없습니다."),
    FAQ_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "FAQ CATEGORY NOT FOUND", "FAQ 카테고리를 찾을 수 없습니다."),
    INVALID_FAQ_ITEM_RELATION(HttpStatus.BAD_REQUEST, "INVALID FAQ ITEM RELATION", "해당 FAQ 카테고리의 아이템을 찾을 수 없습니다."),
    FAQ_CATEGORY_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "FAQ CATEGORY ALREADY EXISTS", "해당 FAQ 카테고리가 이미 존재합니다."),
    UPDATE_ORDER_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "UPDATE ORDER INVALID FORMAT", "넘겨줘야하는 정보가 잘못되었습니다."),

    // FAQ 카드 관련 응답
    FAQ_CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "FAQ CARD NOT FOUND", "FAQ 카드를 찾을 수 없습니다."),

    // 아이템 관련 응답
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "ITEM NOT FOUND", "아이템을 찾을 수 없습니다."),
    UNMATCHED_SELLER_ITEM(HttpStatus.BAD_REQUEST, "UNMATCHED SELLER ITEM", "셀러가 해당 아이템을 가지고 있지 않습니다."),
    UNMATCHED_ITEM_FAQCATEGORY(HttpStatus.BAD_REQUEST, "UNMATCHED ITEM FAQCATEGORY", "아이템이 해당 FAQ 카테고리를 가지고 있지 않습니다."),

    //질문관리창 관련 응당
    QUESTION_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION CATEGORY NOT FOUND", "질문 카테고리를 찾을 수 없습니다."),
    QUESTION_TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION TAG NOT FOUND", "질문 카테고리의 소분류 태그를 찾을 수 없습니다."),
    INVALID_QUESTION_ITEM_RELATION(HttpStatus.BAD_REQUEST, "INVALID QUESTION ITEM RELATION", "해당 질문 카테고리의 아이템을 찾을 수 없습니다."),
    TALKBOX_ALREADY_OPENED(HttpStatus.BAD_REQUEST, "TALKBOX ALREADY OPENED", "톡박스가 최초로 열린 이후에는 질문 카테고릴 수정할 수 없습니다."),
    TALKBOX_CLOSED(HttpStatus.BAD_REQUEST, "TALKBOX CLOSED", "톡박스가 닫혀있습니다."),
    QUESTION_INVALID_RELATION(HttpStatus.BAD_REQUEST, "QUESTION INVALID RELATION", "해당 질문을 조회할 수 없습니다."),
    QUESTIONTAG_INVALID_RELATION(HttpStatus.BAD_REQUEST,"QUESTIONTAG INVALID RELATION", "해당 질문 태그를 조회할 수 없습니다."),
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, "ANSWER NOT FOUND", "해당 질문의 답변을 찾을 수 없습니다."),
    INVALID_TALKBOX_REQUEST(HttpStatus.BAD_REQUEST, "INVALID TALKBOX REQUEST", "다시 INITIAL로 되돌릴 수 없습니다."),

    // 정렬 관련 응답
    UNSUPPORTED_SORT_TYPE(HttpStatus.BAD_REQUEST, "UNSUPPORTED SORT TYPE", "가능한 정렬이 아닙니다."),

    // redis 관련 응답
    REDIS_KEY_NOT_FOUND(HttpStatus.NOT_FOUND,"REDIS KEY NOT FOUND" ,"REDIS 키를 찾을 수 없습니다." ),

    //토큰 관련 응답
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"EXPIRED TOKEN" ,"만료된 토큰입니다." ),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID TOKEN","유효하지 않은 토큰입니다." ),

    //AI 관련 응답
    AI_RESPONSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"AI RESPONSE ERROR" ,"AI 응답 생성에 실패했거나 응답 형식에 문제가 있습니다." ),
    AI_CLASSIFICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"AI CLASSIFICATION ERROR" ,"AI가 존재하지 않는 태그를 이용해 분류를 시도했습니다." ),

    //API 요청 관련 에러 응답
    NEED_TO_SIGN_UP(HttpStatus.OK,"NEED_TO_SIGN_UP" ,"회원이 아닙니다." ),
    GET_KAKAO_TOKEN_FAILED(HttpStatus.BAD_REQUEST,"GET KAKAO TOKEN FAILED" ,"카카오 로그인 토큰을 요청하는 과정에서 문제가 일어났습니다." ),
    GET_KAKAO_USER_PROFILE_FAILED(HttpStatus.BAD_REQUEST,"GET KAKAO USER PROFILE FAILED" ,"카카오 유저 정보를 조회하는 과정에서 문제가 일어났습니다." );



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
