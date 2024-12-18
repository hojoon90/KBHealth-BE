package com.healthcare.kb.exception;

import com.healthcare.kb.constant.MessageConst;

public class ConflictException extends RuntimeException{

    public ConflictException(MessageConst message) {
        super(message.getMessage());
    }
}
