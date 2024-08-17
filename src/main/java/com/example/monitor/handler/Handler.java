package com.example.monitor.handler;

import com.example.monitor.dto.Metric;

/**
 * 메트릭 데이터의 각 단계별 처리를 위한 핸들러 추상체
 */
public abstract class Handler {

    // 다음 체인으로 연결될 핸들러
    protected Handler nextHandler = null; // 처음 생성 당시에는 후순위 연결 핸들러가 없으니 null 초기화

    // 생성자를 통해 연결시킬 핸들러를 순서대로 등록
    public Handler setNext(Handler handler) {
        this.nextHandler = handler;
        return handler; // 메서드 체이닝 구성을 위해 인자 그대로 반환
    }

    // 자식 핸들러에서 구체화 하는 추상 메서드
    protected abstract void process(Metric metric);

    // 핸들러가 요청에 대해 처리하는 메서드
    public void handle(Metric metric) {
        process(metric);

        // 만일 핸들러가 연결된게 있다면 다음 핸들러로 책임을 떠넘긴다
        if (nextHandler != null)
            nextHandler.handle(metric);
    }
}