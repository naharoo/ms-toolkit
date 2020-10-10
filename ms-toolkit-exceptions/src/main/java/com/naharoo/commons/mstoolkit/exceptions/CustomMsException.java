package com.naharoo.commons.mstoolkit.exceptions;

public class CustomMsException extends MsException {

    public CustomMsException(final ExceptionType type, final String message, final Throwable cause) {
        super(type, message, cause);
    }

    public static CustomMsException createInstance(
            final ExceptionType type,
            final String message,
            final Throwable cause
    ) {
        return new CustomMsException(type, message, cause);
    }

    public static CustomMsException createInstance(final ExceptionType type, final String message) {
        return createInstance(type, message, null);
    }

    public static CustomMsException createInstance(final ExceptionType type, final Throwable cause) {
        return createInstance(type, null, cause);
    }

    public static CustomMsException createInstance(final ExceptionType type) {
        return createInstance(type, null, null);
    }
}
