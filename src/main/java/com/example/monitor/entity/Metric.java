package com.example.monitor.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String instance;

    @Column(nullable = false)
    private String application;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String methodName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetricType metricType; // RATE or AVERAGE_TIME

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResultType resultType; // SUCCESS or FAILURE

    @Column
    private String exception; // 결과가 FAILURE 일 경우, 반환한 예외 확인(SUCCESS 일 경우에는 null)

    @Column
    private Double value; // 메트릭 타입에 따른 값

    public enum MetricType {
        CALL_COUNT,          // 시간당 호출 빈도수
        AVERAGE_TIME   // 평균 응답 시간
    }

    public enum ResultType {
        SUCCESS,
        FAILURE
    }
}
