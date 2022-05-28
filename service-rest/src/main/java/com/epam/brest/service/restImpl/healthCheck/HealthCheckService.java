package com.epam.brest.service.restImpl.healthCheck;

import com.epam.brest.service.restImpl.healthCheck.dto.Health;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HealthCheckService implements InitializingBean {

    @Value("${rest.server.protocol}")
    private String protocol;
    @Value("${rest.server.host}")
    private String host;
    @Value("${rest.server.port}")
    private Integer port;

    private String rootUrl;

    private final RestTemplate restTemplate;

    public HealthCheckService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void afterPropertiesSet() {
        this.rootUrl = String.format("%s://%s:%d", protocol, host, port);
    }

    public boolean isServerHealthy() {
        ResponseEntity<Health> health = restTemplate.getForEntity(rootUrl + "/actuator/health", Health.class);
        Health body = health.getBody();

        return body != null && body.getStatus().equalsIgnoreCase("up");
    }
}
