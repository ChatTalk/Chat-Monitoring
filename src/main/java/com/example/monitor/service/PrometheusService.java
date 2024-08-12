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

    @Autowired
    public PrometheusService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<List<String>> fetchMetrics() {
        List<String> queryParams = new ArrayList<>();
        queryParams.add("book_get_count_total[5h]");
        queryParams.add("books_get_count_total[5h]");
        queryParams.add("loan_create_count_total[5h]");
        queryParams.add("loan_update_count_total[5h]");

        // 여기서 각각의 것들 객체화해서

        return Flux.fromIterable(queryParams)
                .flatMap(queryParam -> {
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
                            .doOnNext(responseBody -> {
                                // JSON 포맷팅
                                try {
                                    ObjectMapper mapper = new ObjectMapper();
                                    Object json = mapper.readValue(responseBody, Object.class);
                                    ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                                    String prettyJson = writer.writeValueAsString(json);
                                    System.out.println(prettyJson);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                })
                .collectList();
    }
}
