package com.example.monitor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class PrometheusService {

    private final WebClient webClient;

    @Autowired
    public PrometheusService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<String> fetchMetrics() {
//        String query = URLEncoder.encode("sum(http_requests_count{endpoint=\"/api/user\"}) by (endpoint)", StandardCharsets.UTF_8);

//        return webClient.get()
////                .uri("/api/v1/query?query=" + query)
//                .uri("/api/v1/query?query=up") // 원하는 쿼리(PromQL)로 수정 가능
//                .retrieve()
//                .bodyToMono(String.class);
        String query = "rate(book_get_count_total[5h])";

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/query")
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class).flatMap(errorBody -> Mono.error(new RuntimeException("Error response: " + errorBody))))
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
    }
}
