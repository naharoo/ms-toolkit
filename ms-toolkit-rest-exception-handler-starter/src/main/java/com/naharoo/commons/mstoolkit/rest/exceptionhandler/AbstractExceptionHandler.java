package com.naharoo.commons.mstoolkit.rest.exceptionhandler;

import com.naharoo.commons.mstoolkit.exceptions.IssueType;
import com.naharoo.commons.mstoolkit.exceptions.MsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public abstract class AbstractExceptionHandler {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected ResponseEntity<?> handleMsException(final MsException exception, final HttpServletRequest request) {
        final HttpServletRequestInfoBuilder infoBuilder = HttpServletRequestInfoBuilder.newInstance(request);
        logTrace(exception, infoBuilder);

        final IssueType type = exception.getReportingIssueType();
        final int statusCode = type.statusCode();
        final ResponseEntity<ApiErrorResponse> response = ResponseEntity.status(statusCode).body(new ApiErrorResponse(
                statusCode,
                new HashSet<>(exception.getTypes()),
                exception.getLocalizedMessage() != null ? singletonList(exception.getLocalizedMessage()) : emptyList(),
                LocalDateTime.now()
        ));

        logInfo(exception, infoBuilder);
        return response;
    }

    protected void logTrace(final Exception exception, final HttpServletRequestInfoBuilder infoBuilder) {
        final String info = infoBuilder.build();
        logger.trace(
                "Handling {}... (message:'{}', request:{})",
                exception.getClass().getSimpleName(),
                exception.getMessage(),
                info,
                exception
        );
    }

    protected void logError(final Exception exception, final HttpServletRequestInfoBuilder infoBuilder) {
        final String info = infoBuilder.build();
        logger.error("Done handling {}. (request:{})", exception.getClass().getSimpleName(), info, exception);
    }

    protected void logInfo(final Exception exception, final HttpServletRequestInfoBuilder infoBuilder) {
        final String info = infoBuilder.build();
        logger.info("Done handling {}. (request:{})", exception.getClass().getSimpleName(), info);
    }
}
