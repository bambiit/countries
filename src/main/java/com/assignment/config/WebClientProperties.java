package com.assignment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix="webclient")
public class WebClientProperties {
    private String restcountriesUrl;
}
