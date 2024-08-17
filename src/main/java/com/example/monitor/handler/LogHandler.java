package com.example.monitor.handler;

import com.example.monitor.dto.Metric;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogHandler extends Handler {

    @Override
    protected void process(Metric metric) {

        String output = String.format(
                "Instance: %s, " +
                        "Application: %s, " +
                        "ClassName: %s, " +
                        "MethodName: %s, " +
                        "MetricType: %s, " +
                        "ResultType: %s, " +
                        "Exception: %s, " +
                        "Value: %f, " +
                        "TimeStamp: %s",
                metric.getInstance(),
                metric.getApplication(),
                metric.getClassName(),
                metric.getMethodName(),
                metric.getMetricType(),
                metric.getResultType(),
                metric.getException(),
                metric.getValue(),
                metric.getTime()
        );

        // 메트릭 타입 및 결과값에 따른 로그 출력
        // 호출수 기준은 결과 타입에 따라서
        if (metric.getMetricType() == Metric.MetricType.CALL_COUNT) {
            if (metric.getResultType().equals(Metric.ResultType.SUCCESS)) {
                log.info(output);
            } else if (metric.getResultType().equals(Metric.ResultType.FAILURE)) {
                log.warn(output);
            }
        // 빈도수 기준은 예외 발생 여부에 따라서
        } else if (metric.getMetricType() == Metric.MetricType.AVERAGE_TIME) {
            if (metric.getException().equals("none")) {
                log.info(output);
            } else {
                log.warn(output);
            }
        }
    }
}
