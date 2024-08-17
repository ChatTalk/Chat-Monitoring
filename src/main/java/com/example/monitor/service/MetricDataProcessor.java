package com.example.monitor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MetricDataProcessor {

    private final MetricDataCollector metricDataCollector;

    public void processMetrics() {
        metricDataCollector.fetchMetrics()
                .subscribe(response -> {
                    // JSON 파싱 및 데이터 처리 로직 추가
                    response.stream()
                            .map(metric -> {
                                // 각 Metric 객체의 필드를 추출하고 필요한 변환을 수행합니다.
                                return String.format(
                                        "Instance: %s, " +
                                                "Application: %s, " +
                                                "ClassName: %s, " +
                                                "MethodName: %s, " +
                                                "MetricType: %s, " +
                                                "ResultType: %s, " +
                                                "Exception: %s, " +
                                                "Value: %f" +
                                                "TimeStamp: %s"
                                        ,
                                        metric.getInstance(),
                                        metric.getApplication(),
                                        metric.getClassName(),
                                        metric.getMethodName(),
                                        metric.getMetricType(),
                                        metric.getResultType(),
                                        metric.getException(),
                                        metric.getValue(),
                                        metric.getTime()
                                );
                            })
                            .forEach(System.out::println); // 변환된 문자열을 출력
                });
    }
}
