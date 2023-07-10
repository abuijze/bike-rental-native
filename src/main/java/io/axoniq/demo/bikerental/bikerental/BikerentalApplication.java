package io.axoniq.demo.bikerental.bikerental;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.axoniq.demo.bikerental.bikerental.history.BikeHistory;
import io.axoniq.demo.bikerental.bikerental.query.BikeStatus;
import org.axonframework.common.jdbc.ConnectionProvider;
import org.axonframework.config.Configuration;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.tokenstore.jdbc.GenericTokenTableFactory;
import org.axonframework.eventhandling.tokenstore.jdbc.JdbcTokenStore;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@ImportRuntimeHints(BikerentalApplication.HintsRegistrar.class)
@EntityScan(basePackageClasses = {BikeHistory.class, BikeStatus.class})
@SpringBootApplication
public class BikerentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikerentalApplication.class, args);
    }

    @Autowired
    public void configure(ObjectMapper objectMapper) {
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                                           ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS);
    }

    @Autowired
    public void eventProcessingModule(EventProcessingConfigurer configurer) {
        configurer.usingPooledStreamingEventProcessors()
                  .registerPooledStreamingEventProcessorConfiguration((c, processor) ->
                                                                              processor.workerExecutor(workerExecutor()));
    }
//
//    @Bean
//    public EventProcessorControlService processorProvider(AxonServerConnectionManager axonServerConnectionManager,
//                                                          EventProcessingConfiguration eventProcessingConfiguration,
//                                                          Configuration axonConfig,
//                                                          AxonServerConfiguration configuration) {
//        var eventProcessorControlService = new EventProcessorControlService(axonServerConnectionManager, eventProcessingConfiguration, configuration.getContext()) {
//            @Override
//            public Supplier<EventProcessorInfo> infoSupplier(EventProcessor processor) {
//                Supplier<EventProcessorInfo> parentSupplier = super.infoSupplier(processor);
//                return () -> {
//                    EventProcessorInfo parentInfo = parentSupplier.get();
////                    logger.info("Retrieved information: {}", parentInfo);
//                    return parentInfo;
//                };
//            }
//        };
//        logger.info("Control Service registered.");
//        axonConfig.onStart(Phase.INSTRUCTION_COMPONENTS + 1, eventProcessorControlService::start);
//        return eventProcessorControlService;
//    }

    @Qualifier("workerExecutor")
    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService workerExecutor() {
        return Executors.newScheduledThreadPool(2);
    }

    @Bean
    public JdbcTokenStore tokenStore(ConnectionProvider connectionProvider, Configuration configuration) {
        var tokenStore = JdbcTokenStore.builder()
                                       .connectionProvider(connectionProvider)
                                       .serializer(configuration.serializer())
                                       .build();
        configuration.onStart(Integer.MIN_VALUE, () -> tokenStore.createSchema(GenericTokenTableFactory.INSTANCE));
        return tokenStore;
    }

    public static class HintsRegistrar implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.reflection().registerType(BikeStatus.class,
                                            MemberCategory.DECLARED_FIELDS,
                                            MemberCategory.INTROSPECT_DECLARED_METHODS,
                                            MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS);
        }
    }

}
