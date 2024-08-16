package com.example.monitor.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Query {
    BOOK_GET_COUNT_TOTAL("book_get_count_total"), // 메소드 호출 횟수 시간당 비율
    BOOK_GET_TIMED_SECONDS_SUM("book_get_timed_seconds_sum"), // 메소드 실행 시간 평균 비율
    BOOKS_GET_COUNT_TOTAL("books_get_count_total"),
    BOOKS_GET_TIMED_SECONDS_SUM("books_get_timed_seconds_sum");

    private final String queryParam;

    public String getPrometheusQuery() {
        return "sum(rate(" + queryParam + "[30s])) by (application, class, exception, instance, job, method, result)";
    }
}
