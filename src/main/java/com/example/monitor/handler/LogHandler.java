package com.example.monitor.handler;

import com.example.monitor.constant.StandardValue;
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

        if (metric.getResultType() == Metric.ResultType.SUCCESS) {
            if (metric.getValue() >= StandardValue.SUCCESS_STANDARD) {
                log.info(output);
            } else {
                log.warn(output);
            }
        } else if (metric.getResultType() == Metric.ResultType.FAILURE) {
            if (metric.getValue() >= StandardValue.FAILURE_STANDARD) {
                log.warn(output);
            } else {
                log.error(output);
            }
        }
    }
}
