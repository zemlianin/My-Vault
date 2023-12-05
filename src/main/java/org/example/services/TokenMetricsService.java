package org.example.services;

import io.micrometer.core.instrument.DistributionSummary;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;

import java.time.Instant;

@Component
public class TokenMetricsService {
    private final DistributionSummary timeToUseTokenSummary;
    private final Counter overdueTokenCounter;
    private final Counter reuseTokenCounter;

    public TokenMetricsService(CompositeMeterRegistry meterRegistry) {
        timeToUseTokenSummary = DistributionSummary.builder("time.to.use.token")
                .publishPercentiles(0.5, 0.8, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        overdueTokenCounter = meterRegistry.counter("amount.of.attempt.to.get.secret.by.overdue.token");
        reuseTokenCounter = meterRegistry.counter("amount.of.attempt.to.reuse.token");
    }

    public void incrementOverdueTokenUseCounter() {
        overdueTokenCounter.increment();
    }

    public void IncrementReuseOldTokenCounter() {
        reuseTokenCounter.increment();
    }

    public void addTokenUsageDuration(long createAt) {
        var durationInDays = (Instant.now().getEpochSecond() - createAt);
        timeToUseTokenSummary.record(durationInDays);
    }
}
