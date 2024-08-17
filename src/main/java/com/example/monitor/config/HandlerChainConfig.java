package com.example.monitor.config;

import com.example.monitor.handler.Handler;
import com.example.monitor.handler.PrintHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerChainConfig {

    @Bean
    public Handler printHandler() {
        return new PrintHandler();
    }

    @Bean
    public Handler handlerChain() {
        Handler printHandler = printHandler();

        // 핸들러 체인 구성
        // 다음 핸들러 체인(저장 핸들러) 구현 시, null 대신 할당
        printHandler.setNext(null);

        return printHandler; // 체인의 시작 핸들러 반환
    }
}
