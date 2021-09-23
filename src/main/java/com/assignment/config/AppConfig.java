package com.assignment.config;

import com.assignment.services.AppService;
import com.assignment.services.AppServiceContract;
import com.assignment.services.repositories.CountryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public AppServiceContract appService(CountryRepository repository) {
        return new AppService(repository);
    }
}
