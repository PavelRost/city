package org.rostfactory.orchestrator.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rostfactory.orchestrator.service.OrchestratorService;
import org.rostfactory.orchestrator.service.servicetype.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrchestratorServiceImpl implements OrchestratorService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CitizenServiceType citizenServiceType;
    private final BankServiceType bankServiceType;
    private final PassportServiceType passportServiceType;
    private final SchoolServiceType schoolServiceType;
    private final PoliceServiceType policeServiceType;
    private final HouseServiceType houseServiceType;
    private final CarServiceType carServiceType;

    @KafkaListener(topics = "result", groupId = "delete")
    public void listener (ConsumerRecord<String, String> record) {
        String[] topicKeys = record.key().split("/");
        String[] topicValues = record.value().split("/");
        ServiceType currentMicroService = getServiceByName(topicKeys[0]);
        String operation = topicKeys[1];
        String result = topicValues[0];
        String citizenId = topicValues[1];
        if (operation.equals("delete")) {
            if (result.equals("true")) {
                sendDeleteEventNextService(currentMicroService, citizenId);
            } else {
                if (currentMicroService.getNameService().equals("citizen")) return;
                sendRollbackEventPrevService(currentMicroService, citizenId);
            }
        } else if (operation.equals("rollback")) {
            if (currentMicroService.getNameService().equals("citizen")) return;
            sendRollbackEventPrevService(currentMicroService, citizenId);
        }
    }

    private void sendDeleteEventNextService(ServiceType currentMicroService, String citizenId) {
        String nextServiceName = currentMicroService.getNextService();
        if (nextServiceName.isEmpty()) return;
        ServiceType nextService = getServiceByName(nextServiceName);
        String serviceEntity = nextService.getServiceEntity();
        int partition = nextService.getTopicPartition();
        kafkaTemplate.send("event", partition,"delete/" + serviceEntity, citizenId);
    }

    private void sendRollbackEventPrevService(ServiceType currentMicroService, String citizenId) {
        String prevServiceName = currentMicroService.getPrevService();
        if (prevServiceName.isEmpty()) return;
        ServiceType nextService = getServiceByName(prevServiceName);
        String serviceEntity = nextService.getServiceEntity();
        int partition = nextService.getTopicPartition();
        kafkaTemplate.send("event", partition,"rollback/" + serviceEntity, citizenId);
    }

    private ServiceType getServiceByName(String currentMicroService) {
        return switch (currentMicroService) {
            case "citizen" -> citizenServiceType;
            case "bank" -> bankServiceType;
            case "passport" -> passportServiceType;
            case "school" -> schoolServiceType;
            case "police" -> policeServiceType;
            case "house" -> houseServiceType;
            case "car" -> carServiceType;
            default -> throw new IllegalStateException("Unexpected value: " + currentMicroService);
        };
    }
}
