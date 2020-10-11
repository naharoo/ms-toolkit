package com.naharoo.commons.mstoolkit.exceptions;

public enum CommonExceptionType implements ExceptionType {
    RESOURCE_NOT_FOUND(404),
    RESOURCE_ALREADY_EXISTS(409),
    RESOURCE_NOT_VIABLE(410),
    PRECONDITION_VIOLATED(412),
    DATA_INTEGRITY_CONSTRAINT_VIOLATED(400),
    NOT_READABLE_REQUEST_BODY(400),
    REQUEST_METHOD_NOT_SUPPORTED(405),
    REQUEST_DATA_TYPE_MISMATCH(400),
    MEDIA_TYPE_NOT_ACCEPTABLE(406),
    MEDIA_TYPE_NOT_SUPPORTED(415),
    REQUEST_HANDLER_MISSING(404);

    private final int statusCode;

    CommonExceptionType(final int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String asString() {
        return name();
    }

    @Override
    public int statusCode() {
        return this.statusCode;
    }

}
