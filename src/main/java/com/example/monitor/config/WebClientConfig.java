package com.example.monitor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${prometheus.url}")
    private String prometheusUrl;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .baseUrl(prometheusUrl) // 기본 Prometheus 서버 URL 설정
                .defaultHeader("Accept", "application/json"); // 기본 요청 헤더 설정
    }
}
