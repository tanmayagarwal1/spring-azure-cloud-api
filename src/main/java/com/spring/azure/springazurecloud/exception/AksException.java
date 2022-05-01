package com.spring.azure.springazurecloud.exception;

public class AksException extends RuntimeException{
    public AksException() {
        super();
    }

    public AksException(String message) {
        super(message);
    }

    public AksException(String message, Throwable cause) {
        super(message, cause);
    }

    public AksException(Throwable cause) {
        super(cause);
    }

    protected AksException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
