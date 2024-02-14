package org.rostfactory.school.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rostfactory.school.client.PoliceClient;
import org.rostfactory.school.entity.DriverLicense;
import org.rostfactory.school.repository.DriverSchoolRepository;
import org.rostfactory.school.service.DriverSchoolService;
import org.rostfactory.school.service.OcGateway;
import org.rostfactory.sharemodule.enums.TypeEntry;
import org.rostfactory.sharemodule.enums.TypeOperationInLog;
import org.rostfactory.sharemodule.exception.EntityNotFoundException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverSchoolServiceImpl implements DriverSchoolService {
    private final DriverSchoolRepository repository;
    private final PoliceClient policeClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OcGateway ocGateway;

    @Override
    public List<DriverLicense> findAllDriverLicense() {
        return repository.findAllByDeletedFalse();
    }

    @Override
    public DriverLicense findById(long id) {
        return repository.findDriverLicenseByIdAndDeletedFalse(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public DriverLicense findByCitizenId(long id) {
        return repository.findDriverLicenseByCitizenIdAndDeletedFalse(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public DriverLicense create(DriverLicense license) {
        DriverLicense licenseEntity = repository.save(license);
        policeClient.createAndSendPoliceFile(licenseEntity);
        ocGateway.sendEntryInLog(TypeEntry.LICENSE, TypeOperationInLog.INCREMENT);
        return licenseEntity;
    }

    @Override
    public DriverLicense update(DriverLicense license) {
        if (!repository.existsByIdAndDeletedFalse(license.getId())) throw new EntityNotFoundException();
        return repository.save(license);
    }

    @Override
    public void deleteByCitizenId(long id) {
        DriverLicense license = findByCitizenId(id);
        repository.delete(license);
        ocGateway.sendEntryInLog(TypeEntry.LICENSE, TypeOperationInLog.DECREMENT);
    }

    @Override
    public void deleteByLicenseId(long id) {
        DriverLicense license = findById(id);
        policeClient.deletePoliceFileByCitizenId(license.getCitizenId());
        repository.delete(license);
        ocGateway.sendEntryInLog(TypeEntry.LICENSE, TypeOperationInLog.DECREMENT);
    }

    @Override
    @KafkaListener(topics = "event", groupId = "delete", topicPartitions = {@TopicPartition(topic = "event", partitions = "3")})
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
                DriverLicense driverLicense = repository.findDriverLicenseByCitizenIdAndDeletedTrue(citizenId)
                        .orElseThrow(EntityNotFoundException::new);
                driverLicense.setDeleted(false);
                repository.save(driverLicense);
                ocGateway.sendEntryInLog(TypeEntry.LICENSE, TypeOperationInLog.INCREMENT);
            } catch (Exception e) {
                isSuccessOperation = false;
            }
            kafkaTemplate.send("result", "school/rollback/license", isSuccessOperation + "/" + citizenId);
        }
    }

    private void deleteOperationByEvent(String entity, String idCitizen) {
        if (entity.equals("license")) {
            long citizenId = Long.parseLong(idCitizen);
            boolean isSuccessOperation = true;
            try {
                deleteByCitizenId(citizenId);
            } catch (Exception e) {
                isSuccessOperation = false;
            }
            kafkaTemplate.send("result", "school/delete/license", isSuccessOperation + "/" + citizenId);
        }
    }
}
