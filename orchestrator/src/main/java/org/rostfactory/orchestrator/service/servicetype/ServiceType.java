package org.rostfactory.orchestrator.service.servicetype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Getter
@Component
public abstract class ServiceType {
    private final String nameService;
    private final String serviceEntity;
    private final int topicPartition;
    private final String nextService;
    private final String prevService;
}
