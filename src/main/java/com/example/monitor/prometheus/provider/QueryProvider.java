package com.example.monitor.prometheus.provider;

import com.example.monitor.prometheus.query.Query;

import java.util.List;

public interface QueryProvider {
    List<Query> getAllQueries();
}
