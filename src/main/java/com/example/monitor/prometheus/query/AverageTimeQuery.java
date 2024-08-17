package com.example.monitor.prometheus.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AverageTimeQuery implements Query {
    BOOK_GET_TIMED_SECONDS_SUM("book_get_timed_seconds_sum"), // 메소드 실행 시간 평균 비율
    BOOKS_GET_TIMED_SECONDS_SUM("books_get_timed_seconds_sum");

    private final String queryParam;

    @Override
    public String getPrometheusQuery() {
        return "sum(rate(" + queryParam + "[30s])) by (application, class, exception, instance, job, method, result)";
    }
}
