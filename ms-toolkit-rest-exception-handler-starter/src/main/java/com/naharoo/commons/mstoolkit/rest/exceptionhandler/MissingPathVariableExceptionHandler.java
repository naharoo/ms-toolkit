package com.naharoo.commons.mstoolkit.rest.exceptionhandler;

import com.naharoo.commons.mstoolkit.exceptions.CommonIssueType;
import com.naharoo.commons.mstoolkit.exceptions.IssueType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

@ControllerAdvice
@ConditionalOnProperty(prefix = "ms-toolkit.rest-exception-handler.handlers.enabled", name = "DATA_INTEGRITY_CONSTRAINT_VIOLATED", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(name = "org.springframework.web.bind.MissingPathVariableException")
public class MissingPathVariableExceptionHandler extends AbstractExceptionHandler {

    /**
     * Handle MissingPathVariableException. Thrown when a 'required' request parameter is missing.
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<?> handle(
            final MissingPathVariableException exception,
            final HttpServletRequest request
    ) {
        final HttpServletRequestInfoBuilder infoBuilder = HttpServletRequestInfoBuilder.newInstance(request);
        logTrace(exception, infoBuilder);

        final IssueType type = CommonIssueType.DATA_INTEGRITY_CONSTRAINT_VIOLATED;
        final int statusCode = type.statusCode();

        final ResponseEntity<ApiErrorResponse> response = ResponseEntity.status(statusCode).body(new ApiErrorResponse(
                statusCode,
                singleton(type),
                singletonList(String.format("%s: parameter is missing", exception.getVariableName())),
                LocalDateTime.now()
        ));

        logInfo(exception, infoBuilder);
        return response;
    }
}
