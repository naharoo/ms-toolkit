package com.naharoo.commons.mstoolkit.exceptions;

import java.util.List;

public class CustomMsException extends MsException {

    public CustomMsException(final List<IssueType> types, final String message, final Throwable cause) {
        super(types, message, cause);
    }

    public static CustomMsException createInstance(
            final List<IssueType> types,
            final String message,
            final Throwable cause
    ) {
        return new CustomMsException(types, message, cause);
    }

    public static CustomMsException createInstance(final List<IssueType> types, final String message) {
        return createInstance(types, message, null);
    }

    public static CustomMsException createInstance(final List<IssueType> types, final Throwable cause) {
        return createInstance(types, null, cause);
    }

    public static CustomMsException createInstance(final List<IssueType> types) {
        return createInstance(types, null, null);
    }
}
