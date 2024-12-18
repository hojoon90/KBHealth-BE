package com.healthcare.kb.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("Thread Exception!!! Method : {}, Params.length : {}\n\nException Cause -{}"
                , method.getName(), params.length, ex.getMessage());
        Arrays.stream(params).forEach(param -> log.error("Method: {}, parameter: {}", method, param));
    }
}
