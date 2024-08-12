package com.example.monitor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String application;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private long totalCalls;

    @Column(nullable = false)
    private long exceptionCount;

    @Column(nullable = false)
    private double averageCalls;
}
