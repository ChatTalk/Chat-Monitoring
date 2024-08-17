package com.example.monitor.service;

import com.example.monitor.query.CallCountQuery;
import com.example.monitor.query.Query;
import com.example.monitor.repository.MetricRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PrometheusService {

    private final WebClient webClient;
    private final List<Query> queryList;

    @Autowired
    public PrometheusService(WebClient.Builder webClientBuilder, MetricRepository metricRepository, List<Query> queryList) {
        this.webClient = webClientBuilder.build();
        this.queryList = queryList;
    }

    public Mono<List<String>> fetchMetrics() {
        return Flux.fromIterable(queryList) // List<Query>를 순회
                .flatMap(this::fetchMetric) // 각 쿼리를 수행
                .collectList(); // 결과를 리스트로 수집
    }

    private Mono<String> fetchMetric(Query queryEnum) {
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
                .bodyToMono(String.class);
        // .map(this::formatJson); // 필요한 경우 포맷팅 추가
    }
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
