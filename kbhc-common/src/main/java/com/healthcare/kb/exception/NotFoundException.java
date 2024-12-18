package com.healthcare.kb.exception;


import com.healthcare.kb.constant.MessageConst;

public class NotFoundException extends RuntimeException {
    public NotFoundException(MessageConst message) {
        super(message.getMessage());
    }
}
