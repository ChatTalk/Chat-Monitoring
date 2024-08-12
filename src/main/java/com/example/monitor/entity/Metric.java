package com.example.monitor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String className;

    @Column(nullable = false)
    private String exception;

    @Column(nullable = false)
    private String instance;

    @Column(nullable = false)
    private String job;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String result;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String value;
}
