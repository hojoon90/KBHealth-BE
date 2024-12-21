package com.healthcare.kb.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageConst {
    //SuccessCode
    //200
    RESULT_SUCCESS(200,"OK"),

    //ErrorCode
    //400
    WRONG_PASSWORD(400,"패스워드가 일치하지 않습니다."),
    WRONG_PARAMETER(400,"잘못된 파라미터 입니다."),

    //401
    UNAUTHORIZED_TOKEN(401,"사용할 수 없는 토큰입니다."),

    //403
    FORBIDDEN_AUTHORIZED(403,"권한이 없습니다."),

    //404
    USER_NOT_FOUND(404,"사용자를 찾을 수 없습니다."),
    DATA_NOT_FOUND(404,"데이터를 찾을 수 없습니다."),
    FILE_NOT_FOUND(404,"파일을 찾을 수 없습니다."),

    //409
    USER_DATA_CONFLICT(409,"이미 존재하는 사용자 입니다."),

    //500
    SERVER_PROCESS_ERROR(500,"서버에서 데이터 처리 중 오류가 발생했습니다.");


    private final int statusCode;
    private final String message;

    public String getMessage(Object... arg) {
        return String.format(message, arg);
    }
}

