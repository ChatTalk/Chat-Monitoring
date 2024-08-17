package com.example.monitor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Metric {
    private String instance;
    private String application;
    private String className;
    private String methodName;
    private MetricType metricType; // RATE or AVERAGE_TIME
    private ResultType resultType; // SUCCESS or FAILURE
    private String exception; // 결과가 FAILURE 일 경우, 반환한 예외 확인(SUCCESS 일 경우에는 null)
    private Double value; // 메트릭 타입에 따른 값
    private LocalDateTime timestamp;

    public enum MetricType {
        CALL_COUNT,          // 시간당 호출 빈도수
        AVERAGE_TIME   // 평균 응답 시간
    }

    public enum ResultType {
        SUCCESS,
        FAILURE
    }
}
