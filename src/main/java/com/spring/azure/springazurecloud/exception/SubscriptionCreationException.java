package com.spring.azure.springazurecloud.exception;

public class SubscriptionCreationException extends RuntimeException{
    public SubscriptionCreationException() {
        super();
    }

    public SubscriptionCreationException(String message) {
        super(message);
    }

    public SubscriptionCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubscriptionCreationException(Throwable cause) {
        super(cause);
    }

    protected SubscriptionCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
