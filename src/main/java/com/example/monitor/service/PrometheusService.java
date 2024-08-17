package com.example.monitor.service;

import com.example.monitor.dto.Metric;
import com.example.monitor.query.Query;
import com.example.monitor.util.MetricParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrometheusService {

    private final WebClient webClient;
    private final List<Query> queryList;
    private final MetricParser metricParser;

    @Autowired
    public PrometheusService(WebClient.Builder webClientBuilder, List<Query> queryList, MetricParser metricParser) {
        this.webClient = webClientBuilder.build();
        this.queryList = queryList;
        this.metricParser = metricParser;
    }

//    public Mono<List<Metric>> fetchMetrics() {
//        return Flux.fromIterable(queryList)
//                .flatMap(queryEnum -> fetchMetric(queryEnum)
//                        .map(response -> metricParser.parseMetrics(response, queryEnum)))
//                .flatMap(Flux::fromIterable)
//                .collectList();
//    }

    public Mono<List<Metric>> fetchMetrics() {
        return Flux.fromIterable(queryList)
                .flatMap(this::fetchAndParseMetric)
                .collectList();
    }

    /**
     * Flux 는 여러개, Mono 는 단일
     * @param queryEnum : 입력되는 쿼리문
     * @return : 파싱돼서 생성된 메트릭 DTO 인스턴스
     */
    private Flux<Metric> fetchAndParseMetric(Query queryEnum) {
        String query = queryEnum.getPrometheusQuery();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/query")
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("Error response: " + errorBody)))
                )
                .bodyToMono(String.class)
                .doOnNext(json -> {
                    // 로깅: 쿼리로 얻은 JSON 데이터 로그 출력
                    System.out.println("Received JSON for query: " + query + "\n" + json);
                })
                .flatMapMany(json -> {
                    // MetricType을 쿼리 종류에 맞게 설정
                    Metric.MetricType metricType = queryEnum.getMetricType();

                    // JSON을 파싱하여 Metric 리스트를 반환
                    List<Metric> metrics = parseMetrics(json, metricType);
                    return Flux.fromIterable(metrics); // 여러 개의 Metric을 반환
                });
    }

    private List<Metric> parseMetrics(String jsonResponse, Metric.MetricType metricType) {
        List<Metric> metrics = new ArrayList<>();

        try {
            // JSON 파싱을 위한 기본 구조 파악 (Jackson 사용)
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode resultsNode = rootNode.path("data").path("result");

            if (resultsNode.isArray()) {
                for (JsonNode resultNode : resultsNode) {
                    JsonNode metricNode = resultNode.path("metric");
                    JsonNode valueNode = resultNode.path("value");

                    String instance = metricNode.path("instance").asText();
                    String application = metricNode.path("application").asText();
                    String className = metricNode.path("class").asText();
                    String methodName = metricNode.path("method").asText();
                    String exception = metricNode.path("exception").asText();
                    String result = metricNode.path("result").asText();
                    Double value = valueNode.get(1).asDouble();

                    // ResultType 결정
                    Metric.ResultType resultType = "success".equalsIgnoreCase(result) ?
                            Metric.ResultType.SUCCESS :
                            Metric.ResultType.FAILURE;

                    // Metric DTO 생성
                    Metric metric = new Metric(instance, application, className, methodName, metricType, resultType, exception, value);
                    metrics.add(metric);
                }
            }
        } catch (Exception e) {
            // JSON 파싱 중 예외 발생 시 로깅
            System.err.println("Error parsing JSON response: " + e.getMessage());
        }

        return metrics;
    }

//    public Mono<List<String>> fetchMetrics() {
//        return Flux.fromIterable(queryList) // List<Query>를 순회
//                .flatMap(this::fetchMetric) // 각 쿼리를 수행
//                .collectList(); // 결과를 리스트로 수집
//    }
//
//    private Mono<String> fetchMetric(Query queryEnum) {
//        String query = queryEnum.getPrometheusQuery();
//
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/api/v1/query")
//                        .queryParam("query", query)
//                        .build())
//                .retrieve()
//                .onStatus(
//                        HttpStatusCode::isError,
//                        response -> response.bodyToMono(String.class)
//                                .flatMap(errorBody -> Mono.error(new RuntimeException("Error response: " + errorBody)))
//                )
//                .bodyToMono(String.class);
//    }
//
//    private String formatJson(String responseBody) {
//        try {
//            Object json = objectMapper.readValue(responseBody, Object.class);
//            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
//            return writer.writeValueAsString(json);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "{}";
//        }
//    }

//    private Flux<Metric> parseMetrics(String jsonResponse) {
//        try {
//            JsonNode rootNode = objectMapper.readTree(jsonResponse);
//            List<Metric> entities = new ArrayList<>();
//
//            for (JsonNode metricNode : rootNode.path("data").path("result")) {
//                JsonNode metric = metricNode.path("metric");
//                String application = metric.path("application").asText();
//                String className = metric.path("class").asText();
//                String instance = metric.path("instance").asText();
//                String method = metric.path("method").asText();
//                String exception = metric.path("exception").asText();
//
//                long occurrenceCount = 1;
//                long exceptionCount = exception.equals("none") ? 0 : 1;
//
////                // 기존 엔티티가 존재하는지 확인하고 업데이트
////                Metric existingEntity = entities.stream()
////                        .filter(e -> e.getApplication().equals(application)
////                                && e.getClassName().equals(className)
////                                && e.getInstance().equals(instance)
////                                && e.getMethod().equals(method))
////                        .findFirst()
////                        .orElse(null);
////
////                if (existingEntity != null) {
////                    existingEntity.setOccurrenceCount(existingEntity.getOccurrenceCount() + 1);
////                    existingEntity.setExceptionCount(existingEntity.getExceptionCount() + exceptionCount);
////                } else {
////                    entities.add(new Metric(application, className, instance, method, occurrenceCount, exceptionCount));
////                }
//            }
//
////            metricRepository.saveAll(entities);
//
//            return Flux.fromIterable(entities);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Flux.error(e);
//        }
//    }
}
