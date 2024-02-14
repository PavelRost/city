package org.rostfactory.orchestrator.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface OrchestratorService {
    void listener (ConsumerRecord<String, String> record);
}
