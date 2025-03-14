package com.fantasy.exception;

/**
 * 自定义业务异常类
 */
public class BizRuntimeException extends RuntimeException{
    public BizRuntimeException() {
    }

    public BizRuntimeException(String message) {
        super(message);
    }

    public BizRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizRuntimeException(Throwable cause) {
        super(cause);
    }

    public BizRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}