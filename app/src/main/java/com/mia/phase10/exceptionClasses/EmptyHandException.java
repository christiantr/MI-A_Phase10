package com.mia.phase10.exceptionClasses;

public class EmptyHandException extends Exception {

    public EmptyHandException() {
    }

    public EmptyHandException(String message) {
        super(message);
    }

    public EmptyHandException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyHandException(Throwable cause) {
        super(cause);
    }

    public EmptyHandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
