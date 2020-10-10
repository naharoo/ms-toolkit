package com.naharoo.commons.mstoolkit.rest.exceptionhandler;

import com.naharoo.commons.mstoolkit.exceptions.ResourceAlreadyExistsException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ConditionalOnProperty(prefix = "ms-toolkit.rest-exception-handler.handlers.enabled", name = "RESOURCE_ALREADY_EXISTS", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ResourceAlreadyExistsExceptionHandler extends AbstractExceptionHandler {

    /**
     * Handles ResourceAlreadyExistsException. Thrown when requested resource already exists.
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<?> handle(
            final ResourceAlreadyExistsException exception,
            final HttpServletRequest request
    ) {
        return handleMsException(exception, request);
    }
}
