package org.example.congigurations;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MonitoringConfiguration {
    @Bean
    public MeterRegistry getMeterRegistry() {
        var meterRegistry = new CompositeMeterRegistry();
        return meterRegistry;
    }
}
