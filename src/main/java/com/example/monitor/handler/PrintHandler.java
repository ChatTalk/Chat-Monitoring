package com.example.monitor.handler;

import com.example.monitor.dto.Metric;

public class PrintHandler extends Handler {

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

        System.out.println(output);
    }
}
