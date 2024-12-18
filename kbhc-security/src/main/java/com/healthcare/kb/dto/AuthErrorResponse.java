package com.healthcare.kb.dto;

import com.healthcare.kb.constant.MessageConst;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class AuthErrorResponse<T> {

    private final int resultCode;
    private final String resultMsg;
    private final T data;

    //401
    public static AuthErrorResponse<Void> unauthorized(){
        return AuthErrorResponse.<Void>builder()
                .resultCode(HttpStatus.UNAUTHORIZED.value())
                .resultMsg(MessageConst.UNAUTHORIZED_TOKEN.getMessage())
                .build();
    }

    //403
    public static AuthErrorResponse<Void> forbidden(){
        return AuthErrorResponse.<Void>builder()
                .resultCode(HttpStatus.FORBIDDEN.value())
                .resultMsg(MessageConst.FORBIDDEN_AUTHORIZED.getMessage())
                .build();
    }
}
