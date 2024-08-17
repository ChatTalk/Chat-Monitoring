package com.example.monitor.util;

import com.example.monitor.dto.Metric;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MetricParser {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ObjectMapper objectMapper;

    public MetricParser() {
        this.objectMapper = new ObjectMapper();
    }

    public List<Metric> parseMetrics(String jsonResponse, Metric.MetricType metricType) {
        List<Metric> metrics = new ArrayList<>();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultsNode = rootNode.path("data").path("result");

            if (resultsNode.isArray()) {
                for (JsonNode resultNode : resultsNode) {
                    JsonNode metricNode = resultNode.path("metric");
                    JsonNode valueNode = resultNode.path("value");

                    // 인덱스 0의 값을 Unix 타임스탬프로 변환(+ 로컬 타임)
                    long timestampInSeconds = valueNode.get(0).asLong();
                    LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestampInSeconds), ZoneId.systemDefault());

                    String instance = metricNode.path("instance").asText();
                    String application = metricNode.path("application").asText();
                    String className = metricNode.path("class").asText();
                    String methodName = metricNode.path("method").asText();
                    String exception = metricNode.path("exception").asText();
                    String result = metricNode.path("result").asText();
                    Double value = valueNode.get(1).asDouble();
                    String time = timestamp.format(FORMATTER);

                    // ResultType 결정
                    Metric.ResultType resultType = "success".equalsIgnoreCase(result) ?
                            Metric.ResultType.SUCCESS :
                            Metric.ResultType.FAILURE;

                    // Metric DTO 생성
                    Metric metric = new Metric(instance, application, className, methodName, metricType, resultType, exception, value, time);
                    metrics.add(metric);
                }
            }
        } catch (Exception e) {
            // JSON 파싱 중 예외 발생 시 로깅
            System.err.println("Error parsing JSON response: " + e.getMessage());
        }

        return metrics;
    }
}
