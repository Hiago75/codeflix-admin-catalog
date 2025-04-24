package com.codeflix.admin.catalog.infrastructure.configuration;

import com.codeflix.admin.catalog.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.codeflix.admin.catalog.infrastructure.configuration.properties.amqp.QueueProperties;
import com.codeflix.admin.catalog.infrastructure.services.EventService;
import com.codeflix.admin.catalog.infrastructure.services.impl.RabbitEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {
    @Bean
    @VideoCreatedQueue
    EventService videoCreatedEventService(
            @VideoCreatedQueue final QueueProperties props,
            final RabbitOperations ops
    ) {
        return new RabbitEventService(
                props.getExchange(),
                props.getRoutingKey(),
                ops
        );
    }
}
