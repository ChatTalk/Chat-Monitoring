package com.example.monitor.process;

import com.example.monitor.handler.Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MetricDataProcessor {

    private final MetricDataCollector metricDataCollector;
    private final Handler handlerChain;

    public void processMetrics() {
        metricDataCollector.fetchMetrics()
                .subscribe(response ->
                    // JSON 파싱 및 데이터 처리 로직 추가
                    response.forEach(handlerChain::handle)
                );
    }
}
