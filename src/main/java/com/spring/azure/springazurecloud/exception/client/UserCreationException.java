package com.spring.azure.springazurecloud.exception.client;

public class UserCreationException extends RuntimeException{
    public UserCreationException() {
        super();
    }

    public UserCreationException(String message) {
        super(message);
    }

    public UserCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserCreationException(Throwable cause) {
        super(cause);
    }

    protected UserCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
