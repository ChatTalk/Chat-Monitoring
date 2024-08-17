package com.example.monitor.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CallCountQuery implements Query {
    BOOK_GET_COUNT_TOTAL("book_get_count_total"), // 메소드 호출 횟수 시간당 비율
    BOOKS_GET_COUNT_TOTAL("books_get_count_total");

    private final String queryParam;

    @Override
    public String getPrometheusQuery() {
        return "sum(rate(" + queryParam + "[30s])) by (application, class, exception, instance, job, method, result)";
    }
}
