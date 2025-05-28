package com.codeflix.admin.catalog.infrastructure.configuration;

import com.codeflix.admin.catalog.infrastructure.configuration.properties.google.GoogleStorageProperties;
import com.codeflix.admin.catalog.infrastructure.configuration.properties.storage.StorageProperties;
import com.codeflix.admin.catalog.infrastructure.services.StorageService;
import com.codeflix.admin.catalog.infrastructure.services.impl.GCStorageService;
import com.codeflix.admin.catalog.infrastructure.services.local.InMemoryStorageService;
import com.google.cloud.storage.Storage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {

    @Bean
    @ConfigurationProperties(value = "storage.video-catalog")
    public StorageProperties storageProperties() {
        return new StorageProperties();
    }

    @Bean(name = "storageService")
    @Profile({"development", "test-integration", "test-e2e"})
    public StorageService inMemoryStorageService() {
        return new InMemoryStorageService();
    }

    @Bean(name = "storageService")
    @ConditionalOnMissingBean
    public StorageService gcStorageService(
            final GoogleStorageProperties props,
            final Storage storage
    ) {
        return new GCStorageService(props.getBucket(), storage);
    }
}
