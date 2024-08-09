package com.example.monitor.config;

import com.example.monitor.service.PrometheusDataProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private final PrometheusDataProcessor prometheusDataProcessor;

    @Autowired
    public SchedulerConfig(PrometheusDataProcessor prometheusDataProcessor) {
        this.prometheusDataProcessor = prometheusDataProcessor;
    }

    @Scheduled(fixedRate = 30000) // 30초마다 호출
    public void fetchAndProcessMetrics() {
        prometheusDataProcessor.processMetrics();
    }
}
