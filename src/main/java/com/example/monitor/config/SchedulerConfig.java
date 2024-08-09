package com.example.monitor.config;

import com.example.monitor.service.PrometheusDataProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final PrometheusDataProcessor prometheusDataProcessor;

    @Scheduled(fixedRate = 30000) // 30초마다 호출
    public void fetchAndProcessMetrics() {
        prometheusDataProcessor.processMetrics();
    }
}
