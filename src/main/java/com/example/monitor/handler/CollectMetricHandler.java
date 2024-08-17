package com.example.monitor.handler;

import com.example.monitor.dto.Metric;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CollectMetricHandler extends Handler{

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public CollectMetricHandler(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void process(Metric metric) {

    }
}
