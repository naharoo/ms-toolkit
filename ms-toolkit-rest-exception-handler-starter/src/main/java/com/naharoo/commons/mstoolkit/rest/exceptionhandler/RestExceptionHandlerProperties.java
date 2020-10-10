package com.naharoo.commons.mstoolkit.rest.exceptionhandler;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "ms-toolkit.rest-exception-handler.handlers")
public class RestExceptionHandlerProperties {

    private Map<String, Boolean> enabled;

    public Map<String, Boolean> getEnabled() {
        return enabled;
    }

    public void setEnabled(final Map<String, Boolean> enabled) {
        this.enabled = enabled;
    }
}
