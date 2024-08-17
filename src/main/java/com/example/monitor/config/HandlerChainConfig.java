package com.example.monitor.config;

import com.example.monitor.handler.Handler;
import com.example.monitor.handler.LogHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerChainConfig {

    @Bean
    public Handler logHandler() {
        return new LogHandler();
    }

    @Bean
    public Handler handlerChain() {
        Handler logHandler = logHandler();

        // 핸들러 체인 구성
        // 다음 핸들러 체인(저장 핸들러) 구현 시, null 대신 할당
        logHandler.setNext(null);

        return logHandler; // 체인의 시작 핸들러 반환
    }
}
