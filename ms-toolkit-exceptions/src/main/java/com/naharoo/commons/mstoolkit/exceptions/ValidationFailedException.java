package com.naharoo.commons.mstoolkit.exceptions;

import static java.util.Collections.singletonList;

public class ValidationFailedException extends MsException {

    private static final long serialVersionUID = 1441613319507470057L;
    private static final String DEFAULT_MESSAGE_FORMAT = "'%s' precondition is violated.";

    public ValidationFailedException(final String message, final Throwable cause) {
        super(singletonList(CommonIssueType.PRECONDITION_VIOLATED), message, cause);
    }

    public static ValidationFailedException createInstance(final String precondition, final Throwable cause) {
        return new ValidationFailedException(String.format(DEFAULT_MESSAGE_FORMAT, precondition), cause);
    }

    public static ValidationFailedException createInstance(final String precondition) {
        return new ValidationFailedException(String.format(DEFAULT_MESSAGE_FORMAT, precondition), null);
    }
}
