package io.github.teamomo.momentswebapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // Log the request details
        logger.info("Request: {} {}", request.getMethod(), request.getURI());
        logger.info("Request Headers: {}", request.getHeaders());   // ToDo: remove after testing

        // Proceed with the request
        return execution.execute(request, body);
    }
}
