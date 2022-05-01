package com.spring.azure.springazurecloud.exception;

public class ContainerRegistryException extends RuntimeException{
    public ContainerRegistryException() {
        super();
    }

    public ContainerRegistryException(String message) {
        super(message);
    }

    public ContainerRegistryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContainerRegistryException(Throwable cause) {
        super(cause);
    }

    protected ContainerRegistryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
