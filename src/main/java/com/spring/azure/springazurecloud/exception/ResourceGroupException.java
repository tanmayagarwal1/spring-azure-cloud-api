package com.spring.azure.springazurecloud.exception;

public class ResourceGroupException extends RuntimeException{
    public ResourceGroupException() {
        super();
    }

    public ResourceGroupException(String message) {
        super(message);
    }

    public ResourceGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceGroupException(Throwable cause) {
        super(cause);
    }

    protected ResourceGroupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
