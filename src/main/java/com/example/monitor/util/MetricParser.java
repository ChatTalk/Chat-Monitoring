package com.example.monitor.util;

import com.example.monitor.dto.Metric;
import com.example.monitor.query.Query;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MetricParser {

    private final ObjectMapper objectMapper;

    public MetricParser() {
        this.objectMapper = new ObjectMapper();
    }

    public List<Metric> parseMetrics(String jsonResponse, Query queryEnum) {

        List<Metric> metrics = new ArrayList<>();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            for (JsonNode resultNode : rootNode) {
                JsonNode dataNode = resultNode.path("data");
                JsonNode results = dataNode.path("result");

                for (JsonNode metricNode : results) {
                    JsonNode metricData = metricNode.path("metric");
                    JsonNode valueData = metricNode.path("value");

                    Metric metric = new Metric(
                            metricData.path("instance").asText(),
                            metricData.path("application").asText(),
                            metricData.path("class").asText(),
                            metricData.path("method").asText(),
                            queryEnum.getMetricType(), // 쿼리 타입에 따른 MetricType 설정
                            determineResultType(metricData.path("result").asText()), // SUCCESS or FAILURE
                            metricData.path("exception").asText().equals("none") ? null : metricData.path("exception").asText(),
                            valueData.size() > 1 ? Double.parseDouble(valueData.get(1).asText()) : null
                    );

                    metrics.add(metric);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return metrics;
    }

    private Metric.ResultType determineResultType(String resultType) {
        return resultType.equals("success") ? Metric.ResultType.SUCCESS : Metric.ResultType.FAILURE;
    }
}
