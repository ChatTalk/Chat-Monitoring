package com.example.monitor.process;

import com.example.monitor.dto.Metric;
import com.example.monitor.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MetricDataCollector {

    private final WebClient webClient;
    private final List<Query> queryList;
    private final MetricDataParser metricDataParser;

    @Autowired
    public MetricDataCollector(WebClient.Builder webClientBuilder, List<Query> queryList, MetricDataParser metricDataParser) {
        this.webClient = webClientBuilder.build();
        this.queryList = queryList;
        this.metricDataParser = metricDataParser;
    }

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
//                .doOnNext(json -> {
//                    // 쿼리로 얻은 JSON 메트릭 데이터 로그 출력
//                    System.out.println("Received JSON for query: " + query + "\n" + json);
//                })
                .flatMapMany(json -> {
                    // MetricType 쿼리 종류에 맞게 설정
                    Metric.MetricType metricType = queryEnum.getMetricType();

                    // JSON 파싱하여 Metric 리스트를 반환
                    List<Metric> metrics = metricDataParser.parseMetrics(json, metricType);
                    return Flux.fromIterable(metrics); // 여러 개의 Metric 반환
                });
    }
}
