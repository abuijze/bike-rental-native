package io.axoniq.demo.bikerental.bikerental;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import io.axoniq.demo.bikerental.bikerental.history.BikeHistory;
import io.axoniq.demo.bikerental.bikerental.query.BikeStatus;
import io.grpc.EquivalentAddressGroup;
import org.axonframework.common.jdbc.ConnectionProvider;
import org.axonframework.config.Configuration;
import org.axonframework.config.Configurer;
import org.axonframework.eventhandling.tokenstore.ConfigToken;
import org.axonframework.eventhandling.tokenstore.jdbc.GenericTokenTableFactory;
import org.axonframework.eventhandling.tokenstore.jdbc.JdbcTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeHint;

@TypeHint(types = BikeStatus.class)
@EntityScan(basePackageClasses = {BikeHistory.class, BikeStatus.class})
@SpringBootApplication
public class BikerentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikerentalApplication.class, args);
    }

    @Autowired
    public void configure(ObjectMapper objectMapper) {
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator());;
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

}
