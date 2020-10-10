package com.naharoo.commons.mstoolkit.rest.exceptionhandler;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestInfoBuilder {

    private final HttpServletRequest httpRequest;

    private HttpServletRequestInfoBuilder(HttpServletRequest httpRequest) {
        Assert.notNull(httpRequest, "httpRequest cannot be null.");
        this.httpRequest = httpRequest;
    }

    public static HttpServletRequestInfoBuilder newInstance(HttpServletRequest httpRequest) {
        return new HttpServletRequestInfoBuilder(httpRequest);
    }

    private String getUri() {
        StringBuilder uriBuilder = new StringBuilder(this.httpRequest.getRequestURI());
        String queryString = this.httpRequest.getQueryString();
        if (StringUtils.hasText(queryString)) {
            uriBuilder.append("?").append(queryString);
        }

        return uriBuilder.toString();
    }

    private void appendUri(StringBuilder builder) {
        Assert.notNull(builder, "builder cannot be null.");
        builder.append("uri:'").append(this.getUri()).append("'");
    }

    private String getHttpMethod() {
        return this.httpRequest.getMethod();
    }

    private void appendHttpMethod(StringBuilder builder) {
        Assert.notNull(builder, "builder cannot be null.");
        builder.append("method:'").append(this.getHttpMethod()).append("'");
    }

    private void appendHttpMethodAndUri(StringBuilder builder) {
        Assert.notNull(builder, "builder cannot be null.");
        this.appendHttpMethod(builder);
        builder.append(", ");
        this.appendUri(builder);
    }

    public String build() {
        StringBuilder infoBuilder = new StringBuilder();
        this.appendHttpMethodAndUri(infoBuilder);
        return infoBuilder.toString();
    }

    public String toString() {
        return this.build();
    }
}
