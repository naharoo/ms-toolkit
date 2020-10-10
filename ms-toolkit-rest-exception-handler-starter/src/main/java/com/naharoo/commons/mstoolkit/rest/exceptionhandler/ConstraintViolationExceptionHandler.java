package com.naharoo.commons.mstoolkit.rest.exceptionhandler;

import com.naharoo.commons.mstoolkit.exceptions.CommonExceptionType;
import com.naharoo.commons.mstoolkit.exceptions.ExceptionType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;

@ControllerAdvice
@ConditionalOnProperty(prefix = "ms-toolkit.rest-exception-handler.handlers.enabled", name = "DATA_INTEGRITY_CONSTRAINT_VIOLATED", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(name = "javax.validation.ConstraintViolationException")
public class ConstraintViolationExceptionHandler extends AbstractExceptionHandler {

    private static Set<ConstraintViolation<?>> excludeMultipartFile(final javax.validation.ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                 .stream()
                 .filter(it -> !(it.getInvalidValue() instanceof MultipartFile))
                 .collect(Collectors.toSet());
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handle(
            final ConstraintViolationException exception,
            final HttpServletRequest request
    ) {
        final HttpServletRequestInfoBuilder infoBuilder = HttpServletRequestInfoBuilder.newInstance(request);
        logTrace(exception, infoBuilder);

        final ExceptionType type = CommonExceptionType.DATA_INTEGRITY_CONSTRAINT_VIOLATED;
        final int statusCode = type.statusCode();

        final List<String> messages = excludeMultipartFile(exception)
                .stream()
                .filter(Objects::nonNull)
                .map(constraintViolation -> String.format(
                        "%s: %s",
                        constraintViolation.getPropertyPath().toString(),
                        constraintViolation.getMessage()
                ))
                .collect(Collectors.toList());

        final ResponseEntity<ApiErrorResponse> response = ResponseEntity.status(statusCode).body(new ApiErrorResponse(
                statusCode,
                singleton(type),
                messages,
                LocalDateTime.now()
        ));

        logInfo(exception, infoBuilder);
        return response;
    }
}
