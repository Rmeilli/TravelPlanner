package org.sid.travelplanner.api.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestConfig {
    private static final Logger logger = LoggerFactory.getLogger(RestConfig.class);

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(
                new BufferingClientHttpRequestFactory(
                        new SimpleClientHttpRequestFactory()
                )
        );

        ClientHttpRequestInterceptor loggingInterceptor = (request, body, execution) -> {
            logger.info("Request URI: {}", request.getURI());
            logger.info("Request Method: {}", request.getMethod());
            logger.info("Request Headers: {}", request.getHeaders());

            return execution.execute(request, body);
        };

        restTemplate.setInterceptors(Collections.singletonList(loggingInterceptor));
        return restTemplate;
    }
}