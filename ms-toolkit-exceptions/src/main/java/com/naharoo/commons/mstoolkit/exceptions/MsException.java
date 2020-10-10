package com.naharoo.commons.mstoolkit.exceptions;

import java.util.Objects;

/**
 * Parent for all Exceptions handled by MS Toolkit exception handler.
 * All children must provide non-null {@link com.naharoo.commons.mstoolkit.exceptions.ExceptionType}.
 */
public abstract class MsException extends RuntimeException {

    private final ExceptionType type;

    protected MsException(final ExceptionType type, final String message, final Throwable cause) {
        super(message, cause);

        if (Objects.isNull(type)) {
            throw new IllegalArgumentException("CommonMsException's type cannot be null");
        }
        this.type = type;
    }

    public final ExceptionType getType() {
        return type;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final MsException that = (MsException) other;

        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
