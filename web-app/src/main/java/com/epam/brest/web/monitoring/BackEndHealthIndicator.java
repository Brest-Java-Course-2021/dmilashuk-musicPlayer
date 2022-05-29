package com.epam.brest.web.monitoring;

import com.epam.brest.service.restImpl.healthCheck.HealthCheckService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class BackEndHealthIndicator implements HealthIndicator {

    private final HealthCheckService healthCheckService;

    public BackEndHealthIndicator(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @Override
    public Health health() {
        if (healthCheckService.isServerHealthy()) {
            return Health.up().withDetail("External backEnd svc", "Healthy").build();
        }

        return Health.down().withDetail("External backEnd svc", "Is Not-Healthy").build();
    }
}
