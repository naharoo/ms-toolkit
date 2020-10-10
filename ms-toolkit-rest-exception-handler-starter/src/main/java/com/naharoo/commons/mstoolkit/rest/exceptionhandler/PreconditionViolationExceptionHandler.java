package com.naharoo.commons.mstoolkit.rest.exceptionhandler;

import com.naharoo.commons.mstoolkit.exceptions.PreconditionViolationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ConditionalOnProperty(prefix = "ms-toolkit.rest-exception-handler.handlers.enabled", name = "PRECONDITION_VIOLATED", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class PreconditionViolationExceptionHandler extends AbstractExceptionHandler {

    /**
     * Handles PreconditionViolationException. Thrown when a precondition is violated.
     */
    @ExceptionHandler(PreconditionViolationException.class)
    public ResponseEntity<?> handle(
            final PreconditionViolationException exception,
            final HttpServletRequest request
    ) {
        return handleMsException(exception, request);
    }
}
