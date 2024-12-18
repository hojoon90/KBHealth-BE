package com.healthcare.kb.exception;


import com.healthcare.kb.constant.MessageConst;

public class DataProcessException extends RuntimeException{

    public DataProcessException(MessageConst message) {
        super(message.getMessage());
    }
}
