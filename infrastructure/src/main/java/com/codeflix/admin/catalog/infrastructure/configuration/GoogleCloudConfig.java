package com.codeflix.admin.catalog.infrastructure.configuration;

import com.codeflix.admin.catalog.infrastructure.configuration.properties.GoogleCloudProperties;
import com.codeflix.admin.catalog.infrastructure.configuration.properties.GoogleStorageProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"development", "production"})
public class GoogleCloudConfig {

    @Bean
    @ConfigurationProperties("google.cloud")
    public GoogleCloudProperties googleCloudProperties() {
        return new GoogleCloudProperties();
    }

    @Bean
    @ConfigurationProperties("google.cloud.storage.video-catalog")
    public GoogleStorageProperties googleStorageProperties() {
        return new GoogleStorageProperties();
    }
}
