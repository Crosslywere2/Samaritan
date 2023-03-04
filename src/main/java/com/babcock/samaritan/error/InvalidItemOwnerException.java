package com.babcock.samaritan.error;

public class InvalidItemOwnerException extends Exception{
    public InvalidItemOwnerException() {
    }

    public InvalidItemOwnerException(String message) {
        super(message);
    }

    public InvalidItemOwnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidItemOwnerException(Throwable cause) {
        super(cause);
    }

    public InvalidItemOwnerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
