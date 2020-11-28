package com.naharoo.commons.mstoolkit.rest.exceptionhandler.client;

import feign.Request;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnClass(name = "feign.codec.ErrorDecoder")
public class FeignErrorDecoder implements ErrorDecoder {

    @Autowired
    private MsExceptionFactory msExceptionFactory;

    @Override
    public Exception decode(String methodKey, Response response) {
        final Request request = response.request();

        final int status = response.status();
        if (status < 400) {
            throw new IllegalStateException(
                    "Only errors can be handled by " + FeignErrorDecoder.class.getSimpleName());
        }

        if (status >= 500) {
            return new IllegalStateException(
                    String.format(
                            "Failed to process HTTP %s %s request. %s resource returned %s",
                            request.httpMethod().name(), request.url(), methodKey, status
                    ));
        }

        return msExceptionFactory.createInstance(status, response.body()::asInputStream);
    }
}
