package com.naharoo.commons.mstoolkit.rest.exceptionhandler.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.naharoo.commons.mstoolkit.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MsExceptionFactory {

    private final ObjectMapper objectMapper;

    @Autowired
    public MsExceptionFactory(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public MsException createInstance(final int status, final InputStreamSupplier inputStreamSupplier) {
        try (final InputStream inputStream = inputStreamSupplier.supply()) {
            final ApiErrorResponse apiErrorResponse =
                    objectMapper.readValue(inputStream, ApiErrorResponse.class);

            final Set<String> types = apiErrorResponse.getTypes();
            if (types.isEmpty()) {
                throw new IllegalStateException(
                        "Failed to process HTTP request's response. Unable to create MsException instance.");
            }

            if (types.size() > 1) {
                return CustomMsException.createInstance(
                        convertToIssuesList(types, status), extractErrorMessage(apiErrorResponse));
            }

            final String type = types.iterator().next();
            if (!isCommonIssue(type)) {
                return CustomMsException.createInstance(
                        convertToIssuesList(types, status), extractErrorMessage(apiErrorResponse));
            }

            final CommonIssueType commonIssue = CommonIssueType.valueOf(type);
            switch (commonIssue) {
                case RESOURCE_NOT_FOUND:
                    return new ResourceNotFoundException(extractErrorMessage(apiErrorResponse), null);
                case RESOURCE_ALREADY_EXISTS:
                    return new ResourceAlreadyExistsException(extractErrorMessage(apiErrorResponse), null);
                case RESOURCE_NOT_VIABLE:
                    return new ResourceNotViableException(extractErrorMessage(apiErrorResponse), null);
                case PRECONDITION_VIOLATED:
                    return new PreconditionViolationException(extractErrorMessage(apiErrorResponse), null);
                default:
                    return CustomMsException.createInstance(
                            convertToIssuesList(types, status), extractErrorMessage(apiErrorResponse));
            }

        } catch (final Exception exception) {
            throw new IllegalStateException(exception);
        }
    }

    private boolean isCommonIssue(String type) {
        return Arrays.stream(CommonIssueType.values())
                     .anyMatch(commonType -> commonType.name().equals(type));
    }

    private List<IssueType> convertToIssuesList(Set<String> types, int statusCode) {
        return types.stream()
                    .map(value -> new LocalIssueType(value, statusCode))
                    .collect(Collectors.toList());
    }

    private String extractErrorMessage(final ApiErrorResponse apiErrorResponse) {
        return apiErrorResponse.getMessages().isEmpty()
               ? null
               : apiErrorResponse.getMessages().iterator().next();
    }

    @FunctionalInterface
    public interface InputStreamSupplier {
        InputStream supply() throws IOException;
    }

    private static class ApiErrorResponse {
        private final int statusCode;
        private final Set<String> types;
        private final Set<String> messages;
        private final LocalDateTime timestamp;

        @JsonCreator
        private ApiErrorResponse(
                @JsonProperty("statusCode") final int statusCode,
                @JsonProperty("types") final Set<String> types,
                @JsonProperty("messages") final Set<String> messages,
                @JsonProperty("timestamp") final LocalDateTime timestamp
        ) {
            this.statusCode = statusCode;
            this.types = types;
            this.messages = messages;
            this.timestamp = timestamp;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public Set<String> getTypes() {
            return types;
        }

        public Set<String> getMessages() {
            return messages;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }

    @JsonSerialize(using = LocalIssueType.Serializer.class)
    private static class LocalIssueType implements IssueType {

        private final String value;
        private final int statusCode;

        public LocalIssueType(final String value, final int statusCode) {
            this.value = value;
            this.statusCode = statusCode;
        }

        @Override
        public String asString() {
            return value;
        }

        @Override
        public int statusCode() {
            return statusCode;
        }

        private static class Serializer extends StdSerializer<LocalIssueType> {

            public Serializer() {
                this(null);
            }

            public Serializer(final Class<LocalIssueType> clazz) {
                super(clazz);
            }

            @Override
            public void serialize(
                    final LocalIssueType value,
                    final JsonGenerator jsonGenerator,
                    final SerializerProvider provider
            ) throws IOException {
                jsonGenerator.writeString(Optional.ofNullable(value).map(LocalIssueType::asString).orElse(null));
            }
        }
    }
}
