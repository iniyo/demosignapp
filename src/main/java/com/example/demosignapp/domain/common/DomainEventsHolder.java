package com.example.demosignapp.domain.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ThreadLocal 기반의 도메인 이벤트 홀더
 * UseCaseService 메서드 범위 내에서 발생한 모든 도메인 이벤트를 모은다.
 */
public class DomainEventsHolder {

    // multi thread 환경에서 thread의 동시성 문제를 해결하기 위함. 각 스레드가 별도의 저장공간을 가지고 데이터를 유지함.
    private static final ThreadLocal<List<DomainEvent>> events = ThreadLocal.withInitial(ArrayList::new);

    public static void addEvent(DomainEvent event) {
        events.get().add(event);
    }

    public static List<DomainEvent> getEvents() {
        return Collections.unmodifiableList(events.get());
    }

    // 항상 저장하고 사용한 후에는 클리어 시켜줘야 한다.
    public static void clear() {
        events.get().clear();
    }
}
