package com.naharoo.commons.mstoolkit.rest.exceptionhandler;

import com.naharoo.commons.mstoolkit.exceptions.CommonExceptionType;
import com.naharoo.commons.mstoolkit.exceptions.ExceptionType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;

@ControllerAdvice
@ConditionalOnProperty(prefix = "ms-toolkit.rest-exception-handler.handlers.enabled", name = "DATA_INTEGRITY_CONSTRAINT_VIOLATED", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(name = "org.springframework.web.bind.MethodArgumentNotValidException")
public class MethodArgumentNotValidExceptionHandler extends AbstractExceptionHandler {

    /**
     * Handle MethodArgumentNotValidException. Thrown when an object fails @Valid validation.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(
            final MethodArgumentNotValidException exception,
            final HttpServletRequest request
    ) {
        final HttpServletRequestInfoBuilder infoBuilder = HttpServletRequestInfoBuilder.newInstance(request);
        logTrace(exception, infoBuilder);

        final ExceptionType type = CommonExceptionType.DATA_INTEGRITY_CONSTRAINT_VIOLATED;
        final int statusCode = type.statusCode();

        final List<String> messages = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .filter(Objects::nonNull)
                .map(error -> {
                    if (error instanceof FieldError) {
                        return String.format(
                                "%s.%s: %s",
                                error.getObjectName(),
                                ((FieldError) error).getField(),
                                error.getDefaultMessage()
                        );
                    }

                    return String.format("%s: %s", error.getObjectName(), error.getDefaultMessage());
                })
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
