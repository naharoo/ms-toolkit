package com.naharoo.commons.mstoolkit.exceptions;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

public class ResourceNotViableException extends MsException {

    private static final long serialVersionUID = 1441613319507470057L;
    private static final String DEFAULT_MESSAGE_FORMAT = "%s by given %s is not viable.";

    public ResourceNotViableException(final String message, final Throwable cause) {
        super(singletonList(CommonIssueType.RESOURCE_NOT_VIABLE), message, cause);
    }

    public static ResourceNotViableException createInstance(
            final String resourceName,
            final String resourceIdentifier,
            final Throwable cause
    ) {
        return new ResourceNotViableException(
                String.format(DEFAULT_MESSAGE_FORMAT, resourceName, resourceIdentifier),
                cause
        );
    }

    public static ResourceNotViableException createInstance(
            final String resourceName,
            final String field,
            final Object value,
            final Throwable cause
    ) {
        return createInstance(resourceName, formatFieldValue(field, value), cause);
    }

    public static ResourceNotViableException createInstance(
            final String resourceName,
            final String field,
            final Object value
    ) {
        return createInstance(resourceName, formatFieldValue(field, value), null);
    }

    public static ResourceNotViableException createInstance(
            final Class<?> resourceClass,
            final String field,
            final Object value
    ) {
        return createInstance(resourceClass.getSimpleName(), field, value);
    }

    public static ResourceNotViableException createInstance(
            final String resourceName,
            final Map<String, Object> fieldValuePairs,
            final Throwable cause
    ) {
        return createInstance(resourceName, fieldValuePairsToString(fieldValuePairs), cause);
    }

    public static ResourceNotViableException createInstance(
            final Class<?> resourceClass,
            final Map<String, Object> fieldValuePairs,
            final Throwable cause
    ) {
        return createInstance(resourceClass.getSimpleName(), fieldValuePairs, cause);
    }

    public static ResourceNotViableException createInstance(
            final String resourceName,
            final Map<String, Object> fieldValuePairs
    ) {
        return createInstance(resourceName, fieldValuePairs, null);
    }

    public static ResourceNotViableException createInstance(
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
