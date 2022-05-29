package com.epam.brest.web.monitoring;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Endpoint(id = "custom")
@Component
public class CustomActuatorEndpoint {

    @ReadOperation
    public Map<String, Integer> customEndpoint() {
        var map = new HashMap<String, Integer>();
        map.put("id", RandomUtils.nextInt());
        return map;
    }
}
