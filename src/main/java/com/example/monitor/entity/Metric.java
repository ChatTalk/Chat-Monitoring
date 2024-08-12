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
    @Column(name = "metirc_id")
    private Long id;

    @Column(name = "application")
    private String application;

    @Column(name = "class_name")
    private String className;

    @Column(name = "instance")
    private String instance;

    @Column(name = "method")
    private String method;

    @Column(name = "occurrence")
    private long occurrenceCount;

    @Column(name = "exception")
    private long exceptionCount;

    public Metric(String application, String className, String instance, String method, long occurrenceCount, long exceptionCount) {
        this.application = application;
        this.className = className;
        this.instance = instance;
        this.method = method;
        this.occurrenceCount = occurrenceCount;
        this.exceptionCount = exceptionCount;
    }
}
