package com.example.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrometheusDataProcessor {

    private final PrometheusService prometheusService;

    @Autowired
    public PrometheusDataProcessor(PrometheusService prometheusService) {
        this.prometheusService = prometheusService;
    }

    public void processMetrics() {
        prometheusService.fetchMetrics()
                .subscribe(response -> {
                    // JSON 파싱 및 데이터 처리 로직 추가
                    System.out.println("Received metrics: " + response);
                });
    }
}
