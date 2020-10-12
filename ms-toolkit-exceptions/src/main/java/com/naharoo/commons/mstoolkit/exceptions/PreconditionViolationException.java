package com.naharoo.commons.mstoolkit.exceptions;

import static java.util.Collections.singletonList;

public class PreconditionViolationException extends MsException {

    private static final long serialVersionUID = 1441613319507470057L;
    private static final String DEFAULT_MESSAGE_FORMAT = "'%s' precondition is violated.";

    public PreconditionViolationException(final String message, final Throwable cause) {
        super(singletonList(CommonIssueType.PRECONDITION_VIOLATED), message, cause);
    }

    public static PreconditionViolationException createInstance(final String precondition, final Throwable cause) {
        return new PreconditionViolationException(String.format(DEFAULT_MESSAGE_FORMAT, precondition), cause);
    }

    public static PreconditionViolationException createInstance(final String precondition) {
        return new PreconditionViolationException(String.format(DEFAULT_MESSAGE_FORMAT, precondition), null);
    }
}
