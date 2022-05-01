package com.spring.azure.springazurecloud.exception.apim;

public class ApimException extends RuntimeException{
    public ApimException() {
    }

    public ApimException(String message) {
        super(message);
    }

    public ApimException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApimException(Throwable cause) {
        super(cause);
    }

    public ApimException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
