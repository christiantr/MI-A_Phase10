package com.mia.phase10.exceptionClasses;

public class EmptyCardStackException extends Exception {
    public EmptyCardStackException(String message) {
        super(message);
    }

    public EmptyCardStackException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyCardStackException(Throwable cause) {
        super(cause);
    }

    public EmptyCardStackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EmptyCardStackException() {
    }
}
