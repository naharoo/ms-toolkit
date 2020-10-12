package com.naharoo.commons.mstoolkit.exceptions;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Parent for all Exceptions handled by MS Toolkit exception handler.
 * All children must provide at least one non-null {@link IssueType}.
 */
public abstract class MsException extends RuntimeException {

    private final IssueType reportingIssueType;
    private final List<IssueType> types;

    /**
     * @param types   {@code List issueTypes} related to this exception.
     *                First type from provided list will be considered as a {@literal reportingIssueType}
     * @param message message of the exception. Can be {@literal null}.
     * @param cause   cause of the exception. Can be {@literal null}.
     */
    protected MsException(final List<IssueType> types, final String message, final Throwable cause) {
        super(message, cause);

        if (Objects.isNull(types) || types.isEmpty()) {
            throw new IllegalArgumentException(
                    "CommonMsException's issue types cannot be null and must contain at least one issue type"
            );
        }
        this.types = types;
        this.reportingIssueType = types.get(0);
    }

    public final IssueType getReportingIssueType() {
        return reportingIssueType;
    }

    public List<IssueType> getTypes() {
        return Collections.unmodifiableList(types);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final MsException that = (MsException) o;

        if (!Objects.equals(reportingIssueType, that.reportingIssueType)) {
            return false;
        }
        return Objects.equals(types, that.types);
    }

    @Override
    public int hashCode() {
        int result = reportingIssueType != null ? reportingIssueType.hashCode() : 0;
        result = 31 * result + (types != null ? types.hashCode() : 0);
        return result;
    }
}
