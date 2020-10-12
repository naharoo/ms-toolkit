package com.naharoo.commons.mstoolkit.rest.exceptionhandler;

import com.naharoo.commons.mstoolkit.exceptions.CommonIssueType;
import com.naharoo.commons.mstoolkit.exceptions.IssueType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

@ControllerAdvice
@ConditionalOnProperty(prefix = "ms-toolkit.rest-exception-handler.handlers.enabled", name = "NOT_READABLE_REQUEST_BODY", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(name = "org.springframework.http.converter.HttpMessageNotReadableException")
public class HttpMessageNotReadableExceptionHandler extends AbstractExceptionHandler {

    /**
     * Handle HttpMessageNotReadableException. Thrown when request body is malformed.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handle(
            final HttpMessageNotReadableException exception,
            final HttpServletRequest request
    ) {
        final HttpServletRequestInfoBuilder infoBuilder = HttpServletRequestInfoBuilder.newInstance(request);
        logTrace(exception, infoBuilder);

        final IssueType type = CommonIssueType.NOT_READABLE_REQUEST_BODY;
        final int statusCode = type.statusCode();

        final ResponseEntity<ApiErrorResponse> response = ResponseEntity.status(statusCode).body(new ApiErrorResponse(
                statusCode,
                singleton(type),
                singletonList(exception.getLocalizedMessage()),
                LocalDateTime.now()
        ));

        logInfo(exception, infoBuilder);
        return response;
    }
}
