package com.healthcare.kb.exception;

import com.healthcare.kb.constant.MessageConst;

public class AsyncLockException extends RuntimeException{

    public AsyncLockException(MessageConst message) {
        super(message.getMessage());
    }

}
