package com.spring.azure.springazurecloud.exception.general;

public class RetrievalException extends RuntimeException{
    public RetrievalException() {
        super();
    }

    public RetrievalException(String message) {
        super(message);
    }

    public RetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetrievalException(Throwable cause) {
        super(cause);
    }

    protected RetrievalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
