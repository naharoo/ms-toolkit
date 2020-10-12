package com.naharoo.commons.mstoolkit.rest.exceptionhandler;

import com.naharoo.commons.mstoolkit.exceptions.CommonIssueType;
import com.naharoo.commons.mstoolkit.exceptions.IssueType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

@ControllerAdvice
@ConditionalOnProperty(prefix = "ms-toolkit.rest-exception-handler.handlers.enabled", name = "MEDIA_TYPE_NOT_SUPPORTED", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(name = "org.springframework.web.HttpMediaTypeNotSupportedException")
public class HttpMediaTypeNotSupportedExceptionHandler extends AbstractExceptionHandler {

    /**
     * Handle HttpMediaTypeNotSupportedException. Thrown when request media type is not acceptable.
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handle(
            final HttpMediaTypeNotSupportedException exception,
            final HttpServletRequest request
    ) {
        final HttpServletRequestInfoBuilder infoBuilder = HttpServletRequestInfoBuilder.newInstance(request);
        logTrace(exception, infoBuilder);

        final IssueType type = CommonIssueType.MEDIA_TYPE_NOT_SUPPORTED;
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
