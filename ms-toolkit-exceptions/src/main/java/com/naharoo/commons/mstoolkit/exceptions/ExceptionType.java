package com.naharoo.commons.mstoolkit.exceptions;

import java.io.Serializable;

/**
 * Type of a known exception.
 * <p>
 * Custom implementations of this interface must provide unique String representation of itself via {@link #asString()} method.
 * Those String representations must be globally unique to avoid ambiguity in handlers.
 * <p>
 * If this exception is handled by Rest Exception Handler, then the implementations must also override default method
 * {@link #statusCode()} and provide corresponding HTTP Status Code.
 */
public interface ExceptionType extends Serializable {

    /**
     * Unique String representation of a type.
     * Must be non-null and non-blank.
     *
     * @return the String representation of a type.
     */
    String asString();

    /**
     * Return the HTTP Status Code corresponding to exceptions of this type.
     * Must be overridden if it is going to be used as an API Exception.
     * Otherwise default value <code>-1</code> will be provided.
     *
     * @return HTTP Status Code corresponding to this type of Exceptions or <code>-1</code> if it is not applicable.
     */
    default int statusCode() {
        return -1;
    }
}
