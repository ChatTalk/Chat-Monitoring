package com.example.monitor.config;

import com.example.monitor.query.AverageTimeQuery;
import com.example.monitor.query.CallCountQuery;
import com.example.monitor.query.Query;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class QueryConfig {

    /**
     * 모든 쿼리들 리스트화 및 빈 등록
     */
    @Bean
    public List<Query> queries() {
        return Stream.of(
                AverageTimeQuery.values(),
                CallCountQuery.values()
        ).flatMap(Arrays::stream).collect(Collectors.toList());
    }
}
