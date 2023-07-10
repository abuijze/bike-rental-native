package io.axoniq.demo.bikerental.bikerental.history;

import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class HistoryConfig {

    @Autowired
    public void configure(EventProcessingConfigurer config) {
        config.registerPooledStreamingEventProcessorConfiguration(
                "io.axoniq.demo.bikerental.bikerental.history",
                (c, processor) -> processor.batchSize(100)
        );
    }
}
