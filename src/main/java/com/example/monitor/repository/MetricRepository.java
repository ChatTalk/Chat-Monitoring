package com.example.monitor.repository;

import com.example.monitor.entity.MetricEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricRepository extends JpaRepository<MetricEntity, Long> {
}
