package com.spring.azure.springazurecloud.exception.storage;

public class StorageAccountException extends RuntimeException{
    public StorageAccountException() {
        super();
    }

    public StorageAccountException(String message) {
        super(message);
    }

    public StorageAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageAccountException(Throwable cause) {
        super(cause);
    }

    protected StorageAccountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
