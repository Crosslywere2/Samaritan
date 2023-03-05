package com.babcock.samaritan.error;

public class RequiredArgNotFoundException extends Exception {
    public RequiredArgNotFoundException() {
    }

    public RequiredArgNotFoundException(String message) {
        super(message);
    }

    public RequiredArgNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequiredArgNotFoundException(Throwable cause) {
        super(cause);
    }

    public RequiredArgNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
