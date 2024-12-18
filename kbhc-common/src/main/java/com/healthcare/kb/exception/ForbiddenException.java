package com.healthcare.kb.exception;


import com.healthcare.kb.constant.MessageConst;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(MessageConst message) {
        super(message.getMessage());
    }
}
