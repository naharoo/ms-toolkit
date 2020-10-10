package com.naharoo.commons.mstoolkit.rest.exceptionhandler;

import com.naharoo.commons.mstoolkit.exceptions.CommonExceptionType;
import com.naharoo.commons.mstoolkit.exceptions.ExceptionType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

@ControllerAdvice
@ConditionalOnProperty(prefix = "ms-toolkit.rest-exception-handler.handlers.enabled", name = "REQUEST_HANDLER_MISSING", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(name = "org.springframework.web.servlet.NoHandlerFoundException")
public class NoHandlerFoundExceptionHandler extends AbstractExceptionHandler {

    /**
     * Handle NoHandlerFoundException. Thrown when request media type is not acceptable.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handle(
            final NoHandlerFoundException exception,
            final HttpServletRequest request
    ) {
        final HttpServletRequestInfoBuilder infoBuilder = HttpServletRequestInfoBuilder.newInstance(request);
        logTrace(exception, infoBuilder);

        final ExceptionType type = CommonExceptionType.REQUEST_HANDLER_MISSING;
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
