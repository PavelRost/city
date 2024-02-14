package org.rostfactory.police.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rostfactory.police.client.SchoolClient;
import org.rostfactory.police.dto.DriverLicenseDtoResponse;
import org.rostfactory.police.entity.PoliceFile;
import org.rostfactory.police.repository.PoliceRepository;
import org.rostfactory.police.service.PoliceService;
import org.rostfactory.sharemodule.exception.EntityNotFoundException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PoliceServiceImpl implements PoliceService {
    private final PoliceRepository repository;
    private final SchoolClient schoolClient;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public boolean isExistDriverLicenseByCitizenId(long id) {
        try {
            PoliceFile file = repository.findByCitizenIdAndDeletedFalse(id).orElseThrow(EntityNotFoundException::new);
            DriverLicenseDtoResponse license = schoolClient.getDriverLicenseByCitizenId(id);
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    @Override
    public PoliceFile create(PoliceFile file) {
        return repository.save(file);
    }

    @Override
    public void delete(long citizenId) {
        PoliceFile file = repository.findByCitizenIdAndDeletedFalse(citizenId).orElseThrow(EntityNotFoundException::new);
        repository.delete(file);
    }

    @Override
    public PoliceFile findByCitizenId(long id) {
        return repository.findByCitizenIdAndDeletedFalse(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @KafkaListener(topics = "event", groupId = "delete", topicPartitions = {@TopicPartition(topic = "event", partitions = "4")})
    public void listener(ConsumerRecord<String, String> record) {
        String[] topicKeys = record.key().split("/");
        String operation = topicKeys[0];
        String entity = topicKeys[1];
        String citizenId = record.value();
        if (operation.equals("delete")) {
            deleteOperationByEvent(entity, citizenId);
        } else if (operation.equals("rollback")) {
            rollbackOperationByEvent(entity, citizenId);
        }
    }

    private void rollbackOperationByEvent(String entity, String idCitizen) {
        if (entity.equals("license")) {
            long citizenId = Long.parseLong(idCitizen);
            boolean isSuccessOperation = true;
            try {
                PoliceFile policeFile = repository.findByCitizenIdAndDeletedTrue(citizenId)
                        .orElseThrow(EntityNotFoundException::new);
                policeFile.setDeleted(false);
                create(policeFile);
            } catch (Exception e) {
                isSuccessOperation = false;
            }
            kafkaTemplate.send("result", "police/rollback/license", isSuccessOperation + "/" + citizenId);
        }
    }

    private void deleteOperationByEvent(String entity, String idCitizen) {
        if (entity.equals("license")) {
            long citizenId = Long.parseLong(idCitizen);
            boolean isSuccessOperation = true;
            try {
                delete(citizenId);
            } catch (Exception e) {
                isSuccessOperation = false;
            }
            kafkaTemplate.send("result", "police/delete/license", isSuccessOperation + "/" + citizenId);
        }
    }
}
