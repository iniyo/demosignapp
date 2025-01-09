package com.example.demosignapp.domain.common;

import java.util.Objects;

/**
 * 제네릭 기반 AggregateRoot 구현
 * 자기 자신을 generic으로 사용 A는 반드시 AggregateRoot의 서브 클래스여야 함.    
 * Aggregate의 서브 클래스여야 타입 안전성을 확보할 수 있음.
 */
public abstract class AggregateRoot<A extends AggregateRoot<A>> {

    /**
     * 이벤트를 등록하고 현재 애그리게이트 타입 반환
     *
     * @param event 등록할 이벤트
     */
    protected void registerEvent(DomainEvent event) {
        Objects.requireNonNull(event, "Domain event must not be null");
        DomainEventsHolder.addEvent(event);
    }
}
