package com.naharoo.commons.mstoolkit.rest.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.naharoo.commons.mstoolkit.exceptions.ExceptionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public final class ApiErrorResponse {

    private final int statusCode;
    private final Set<ExceptionType> types;
    private final List<String> messages;
    private final LocalDateTime timestamp;

    @JsonCreator
    public ApiErrorResponse(
            @JsonProperty("statusCode") final int statusCode,
            @JsonProperty("types") final Set<ExceptionType> types,
            @JsonProperty("messages") final List<String> messages,
            @JsonProperty("timestamp") @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") final LocalDateTime timestamp
    ) {
        this.statusCode = statusCode;
        this.types = types;
        this.messages = messages;
        this.timestamp = timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Set<ExceptionType> getTypes() {
        return types;
    }

    public List<String> getMessages() {
        return messages;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final ApiErrorResponse that = (ApiErrorResponse) other;

        if (statusCode != that.statusCode) {
            return false;
        }
        if (!Objects.equals(types, that.types)) {
            return false;
        }
        if (!Objects.equals(messages, that.messages)) {
            return false;
        }
        return Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        int result = statusCode;
        result = 31 * result + (types != null ? types.hashCode() : 0);
        result = 31 * result + (messages != null ? messages.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ApiErrorResponse.class.getSimpleName() + "[", "]")
                .add("statusCode=" + statusCode)
                .add("types=" + types)
                .add("messages=" + messages)
                .add("timestamp=" + timestamp)
                .toString();
    }
}
