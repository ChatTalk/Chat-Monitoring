package com.example.monitor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrometheusDataProcessor {

    private final PrometheusService prometheusService;

    public void processMetrics() {
        prometheusService.fetchMetrics()
                .subscribe(response -> {
                    // JSON 파싱 및 데이터 처리 로직 추가
                    System.out.println("Received metrics: " + response);
                });
    }
}
