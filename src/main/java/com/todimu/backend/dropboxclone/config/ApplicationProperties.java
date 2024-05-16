package com.todimu.backend.dropboxclone.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationProperties {

    @Bean
    @ConfigurationProperties(prefix = "do.space", ignoreUnknownFields = false)
    public DoSpaceProperties getDoSpaceProperties() {
        return new DoSpaceProperties();
    }
}
