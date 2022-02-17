package io.axoniq.demo.bikerental.bikerental;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "axon.axonserver.enabled=false")
public class BikerentalApplicationTests {

    @Test
    void contextLoads() {
    }

}
