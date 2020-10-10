package com.naharoo.commons.mstoolkit.rest.exceptionhandler;

import com.naharoo.commons.mstoolkit.exceptions.ResourceNotViableException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ConditionalOnProperty(prefix = "ms-toolkit.rest-exception-handler.handlers.enabled", name = "RESOURCE_NOT_VIABLE", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ResourceNotViableExceptionHandler extends AbstractExceptionHandler {

    /**
     * Handles ResourceNotViableException. Thrown when requested resource is not viable.
     */
    @ExceptionHandler(ResourceNotViableException.class)
    public ResponseEntity<?> handle(
            final ResourceNotViableException exception,
            final HttpServletRequest request
    ) {
        return handleMsException(exception, request);
    }
}
