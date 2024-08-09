package com.example.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PrometheusService {

    private final WebClient webClient;

    @Autowired
    public PrometheusService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<String> fetchMetrics() {
        return webClient.get()
                .uri("/api/v1/query?query=up") // 원하는 쿼리(PromQL)로 수정 가능
                .retrieve()
                .bodyToMono(String.class);
    }
}
