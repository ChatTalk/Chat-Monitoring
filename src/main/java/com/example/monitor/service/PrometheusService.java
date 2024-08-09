package com.example.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class PrometheusService {

    private final WebClient webClient;

    @Autowired
    public PrometheusService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<String> fetchMetrics() {
//        String query = URLEncoder.encode("sum(http_requests_count{endpoint=\"/api/user\"}) by (endpoint)", StandardCharsets.UTF_8);

        return webClient.get()
//                .uri("/api/v1/query?query=" + query)
                .uri("/api/v1/query?query=up") // 원하는 쿼리(PromQL)로 수정 가능
                .retrieve()
                .bodyToMono(String.class);
    }
}
