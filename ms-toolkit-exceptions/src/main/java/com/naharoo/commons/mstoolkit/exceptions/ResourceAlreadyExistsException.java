package com.naharoo.commons.mstoolkit.exceptions;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ResourceAlreadyExistsException extends MsException {

    private static final long serialVersionUID = 8395113877763828231L;
    private static final String DEFAULT_MESSAGE_FORMAT = "%s already exists by %s.";

    public ResourceAlreadyExistsException(final String message, final Throwable cause) {
        super(CommonExceptionType.RESOURCE_ALREADY_EXISTS, message, cause);
    }

    public static ResourceAlreadyExistsException createInstance(
            final String resourceName,
            final String resourceIdentifier,
            final Throwable cause
    ) {
        return new ResourceAlreadyExistsException(
                String.format(DEFAULT_MESSAGE_FORMAT, resourceName, resourceIdentifier),
                cause
        );
    }

    public static ResourceAlreadyExistsException createInstance(
            final String resourceName,
            final String field,
            final Object value,
            final Throwable cause
    ) {
        return createInstance(resourceName, formatFieldValue(field, value), cause);
    }

    public static ResourceAlreadyExistsException createInstance(
            final String resourceName,
            final String field,
            final Object value
    ) {
        return createInstance(resourceName, formatFieldValue(field, value), null);
    }

    public static ResourceAlreadyExistsException createInstance(
            final Class<?> resourceClass,
            final String field,
            final Object value
    ) {
        return createInstance(resourceClass.getSimpleName(), field, value);
    }

    public static ResourceAlreadyExistsException createInstance(
            final String resourceName,
            final Map<String, Object> fieldValuePairs,
            final Throwable cause
    ) {
        return createInstance(resourceName, fieldValuePairsToString(fieldValuePairs), cause);
    }

    public static ResourceAlreadyExistsException createInstance(
            final Class<?> resourceClass,
            final Map<String, Object> fieldValuePairs,
            final Throwable cause
    ) {
        return createInstance(resourceClass.getSimpleName(), fieldValuePairs, cause);
    }

    public static ResourceAlreadyExistsException createInstance(
            final String resourceName,
            final Map<String, Object> fieldValuePairs
    ) {
        return createInstance(resourceName, fieldValuePairs, null);
    }

    public static ResourceAlreadyExistsException createInstance(
            final Class<?> resourceClass,
            final Map<String, Object> fieldValuePairs
    ) {
        return createInstance(resourceClass.getSimpleName(), fieldValuePairs);
    }

    private static String fieldValuePairsToString(final Map<String, Object> fieldValuePairs) {
        if (Objects.isNull(fieldValuePairs) || fieldValuePairs.isEmpty()) {
            return null;
        }

        return fieldValuePairs
                .entrySet()
                .stream()
                .filter(Objects::nonNull)
                .map(entry -> formatFieldValue(entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(", "));
    }

    private static String formatFieldValue(final String key, final Object value) {
        return String.format(
                "%s: %s",
                key,
                value instanceof String ? String.format("'%s'", value) : value
        );
    }
}
