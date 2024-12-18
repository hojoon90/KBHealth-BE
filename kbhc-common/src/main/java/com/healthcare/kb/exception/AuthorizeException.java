package com.healthcare.kb.exception;

import com.healthcare.kb.constant.MessageConst;

public class AuthorizeException extends RuntimeException{

    public AuthorizeException(MessageConst message) {
        super(message.getMessage());
    }
}
