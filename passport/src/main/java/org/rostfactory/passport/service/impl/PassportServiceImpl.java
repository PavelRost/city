package org.rostfactory.passport.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rostfactory.passport.client.CitizenClient;
import org.rostfactory.passport.dto.CitizenDtoResponse;
import org.rostfactory.passport.dto.PassportDtoResponse;
import org.rostfactory.passport.entity.Passport;
import org.rostfactory.passport.repository.PassportRepository;
import org.rostfactory.passport.service.OcGateway;
import org.rostfactory.passport.service.PassportService;
import org.rostfactory.sharemodule.enums.TypeEntry;
import org.rostfactory.sharemodule.enums.TypeOperationInLog;
import org.rostfactory.sharemodule.exception.EntityNotFoundException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassportServiceImpl implements PassportService {
    private final PassportRepository repository;
    private final CitizenClient citizenClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OcGateway ocGateway;

    @Override
    @Transactional
    public Passport create(Passport passport) {
        Passport passportEntity = repository.save(passport);
        ocGateway.sendEntryInLog(TypeEntry.PASSPORT, TypeOperationInLog.INCREMENT);
        return passportEntity;
    }

    @Override
    public Passport update(Passport passportNew) {
        if (!repository.existsByIdAndDeletedFalse(passportNew.getId())) throw new EntityNotFoundException();
        return repository.save(passportNew);
    }

    @Override
    public void deleteByPassportId(long id) {
        Passport passport = findById(id);
        repository.delete(passport);
        ocGateway.sendEntryInLog(TypeEntry.PASSPORT, TypeOperationInLog.DECREMENT);
    }

    @Override
    public void deleteByCitizenId(long id) {
        Passport passport = repository.findPassportByCitizenIdAndDeletedFalse(id).orElseThrow(EntityNotFoundException::new);
        repository.delete(passport);
        ocGateway.sendEntryInLog(TypeEntry.PASSPORT, TypeOperationInLog.DECREMENT);
    }

    @Override
    public List<Passport> findAllPassports() {
        return repository.findAllByDeletedFalse();
    }

    @Override
    public Passport findById(long id) {
        return repository.findPassportByIdAndDeletedFalse(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Passport findByCitizenId(long id) {
        return repository.findPassportByCitizenIdAndDeletedFalse(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<PassportDtoResponse> findPassportsByLastNameStartLetter(String letter) {
        List<CitizenDtoResponse> citizens = citizenClient.findCitizensByLastNameStartLetter(letter);
        return citizens.stream()
                .map(CitizenDtoResponse::getPassport)
                .toList();
    }

    @Override
    @KafkaListener(topics = "event", groupId = "delete", topicPartitions = {@TopicPartition(topic = "event", partitions = "2")})
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
        if (entity.equals("passport")) {
            long citizenId = Long.parseLong(idCitizen);
            boolean isSuccessOperation = true;
            try {
                Passport passport = repository.findPassportByCitizenIdAndDeletedTrue(citizenId)
                        .orElseThrow(EntityNotFoundException::new);
                passport.setDeleted(false);
                create(passport);
            } catch (Exception e) {
                isSuccessOperation = false;
            }
            kafkaTemplate.send("result", "passport/rollback/passport", isSuccessOperation + "/" + citizenId);
        }
    }

    private void deleteOperationByEvent(String entity, String idCitizen) {
        if (entity.equals("passport")) {
            long citizenId = Long.parseLong(idCitizen);
            boolean isSuccessOperation = true;
            try {
                deleteByCitizenId(citizenId);
            } catch (Exception e) {
                isSuccessOperation = false;
            }
            kafkaTemplate.send("result", "passport/delete/passport", isSuccessOperation + "/" + citizenId);
        }
    }
}
