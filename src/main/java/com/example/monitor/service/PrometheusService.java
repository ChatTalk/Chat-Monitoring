package com.example.monitor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
    private final ObjectMapper objectMapper;

    @Autowired
    public PrometheusService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = new ObjectMapper();
    }

    public Mono<List<String>> fetchMetrics() {
        List<String> queryParams = List.of(
                "book_get_count_total[5h]",
                "books_get_count_total[5h]",
                "loan_create_count_total[5h]",
                "loan_update_count_total[5h]"
        );

        return Flux.fromIterable(queryParams)
                .flatMap(this::fetchMetric)
                .collectList();
    }

    private Mono<String> fetchMetric(String queryParam) {
        String query = "sum(rate(" + queryParam + ")) by (application, class, exception, instance, job, method, result)";

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
                .map(this::formatJson);
    }

    private String formatJson(String responseBody) {
        try {
            Object json = objectMapper.readValue(responseBody, Object.class);
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            return writer.writeValueAsString(json);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
}
