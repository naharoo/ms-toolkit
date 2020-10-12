package com.naharoo.commons.mstoolkit.rest.exceptionhandler;

import com.naharoo.commons.mstoolkit.exceptions.CommonIssueType;
import com.naharoo.commons.mstoolkit.exceptions.IssueType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

@ControllerAdvice
@ConditionalOnProperty(prefix = "ms-toolkit.rest-exception-handler.handlers.enabled", name = "REQUEST_DATA_TYPE_MISMATCH", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(name = "org.springframework.web.method.annotation.MethodArgumentTypeMismatchException")
public class MethodArgumentTypeMismatchExceptionHandler extends AbstractExceptionHandler {

    /**
     * Handle MethodArgumentTypeMismatchException. Thrown while resolving a controller method argument.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handle(
            final MethodArgumentTypeMismatchException exception,
            final HttpServletRequest request
    ) {
        final HttpServletRequestInfoBuilder infoBuilder = HttpServletRequestInfoBuilder.newInstance(request);
        logTrace(exception, infoBuilder);

        final IssueType type = CommonIssueType.REQUEST_DATA_TYPE_MISMATCH;
        final int statusCode = type.statusCode();

        final Class<?> requiredType = exception.getRequiredType();
        final ResponseEntity<ApiErrorResponse> response = ResponseEntity.status(statusCode).body(new ApiErrorResponse(
                statusCode,
                singleton(type),
                singletonList(String.format(
                        "'%s' of type '%s' cannot accept value '%s'",
                        exception.getName(),
                        requiredType == null ? "unknown" : requiredType.getSimpleName(),
                        exception.getValue()
                )),
                LocalDateTime.now()
        ));

        logInfo(exception, infoBuilder);
        return response;
    }
}
