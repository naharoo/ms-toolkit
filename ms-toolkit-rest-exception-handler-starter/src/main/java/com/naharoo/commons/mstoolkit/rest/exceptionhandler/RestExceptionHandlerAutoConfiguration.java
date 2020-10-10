package com.naharoo.commons.mstoolkit.rest.exceptionhandler;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties(RestExceptionHandlerProperties.class)
@ComponentScan("com.naharoo.commons.mstoolkit.rest.exceptionhandler")
@PropertySource("classpath:ms-toolkit-exception-handler-starter.properties")
public class RestExceptionHandlerAutoConfiguration {

}
