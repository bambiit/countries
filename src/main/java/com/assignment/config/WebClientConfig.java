package com.assignment.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(WebClientProperties.class)
@AllArgsConstructor
public class WebClientConfig {
    private final WebClientProperties properties;

    @Bean
    public WebClient webClient() {
        return WebClient
                .builder()
                .baseUrl(properties.getRestcountriesUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
