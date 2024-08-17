package com.example.monitor.query;

import com.example.monitor.dto.Metric;

public interface Query {
    String getPrometheusQuery();
    Metric.MetricType getMetricType();
}