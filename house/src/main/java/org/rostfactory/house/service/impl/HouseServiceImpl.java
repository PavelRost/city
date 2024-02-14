package org.rostfactory.house.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rostfactory.house.entity.House;
import org.rostfactory.house.repository.HouseRepository;
import org.rostfactory.house.service.HouseService;
import org.rostfactory.house.service.OcGateway;
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
public class HouseServiceImpl implements HouseService {
    private final HouseRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OcGateway ocGateway;

    @Override
    public House create(House house) {
        House houseEntity = repository.save(house);
        ocGateway.sendEntryInLog(TypeEntry.HOUSE, TypeOperationInLog.INCREMENT);
        return houseEntity;
    }

    @Override
    @Transactional
    public House update(House house) {
        if (!repository.existsByIdAndDeletedFalse(house.getId())) throw new EntityNotFoundException();
        return repository.save(house);
    }

    @Override
    @Transactional
    public void deleteAllHousesByCitizenId(long id) {
        List<House> houses = repository.findHouseByCitizenIdAndDeletedFalse(id);
        houses.forEach(house -> {
            repository.delete(house);
            ocGateway.sendEntryInLog(TypeEntry.HOUSE, TypeOperationInLog.DECREMENT);
        });
    }

    @Override
    public void deleteByHouseId(long id) {
        House house = findById(id);
        repository.delete(house);
        ocGateway.sendEntryInLog(TypeEntry.HOUSE, TypeOperationInLog.DECREMENT);
    }

    @Override
    public List<House> findAllHouse() {
        return repository.findAllByDeletedFalse();
    }

    @Override
    public House findById(long id) {
        return repository.findByIdAndDeletedFalse(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<House> findByCitizenId(long citizenId) {
        return repository.findHouseByCitizenIdAndDeletedFalse(citizenId);
    }

    @Override
    public House findOwnersByHouseAddress(String address) {
        return repository.findAllByAddress(address).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @KafkaListener(topics = "event", groupId = "delete", topicPartitions = {@TopicPartition(topic = "event", partitions = "5")})
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
        if (entity.equals("house")) {
            long citizenId = Long.parseLong(idCitizen);
            boolean isSuccessOperation = true;
            try {
                List<House> houses = repository.findHouseByCitizenIdAndDeletedTrue(citizenId);
                houses.forEach(house -> {
                    house.setDeleted(false);
                    create(house);
                });
            } catch (Exception e) {
                isSuccessOperation = false;
            }
            kafkaTemplate.send("result", "house/rollback/house", isSuccessOperation + "/" + citizenId);
        }
    }

    private void deleteOperationByEvent(String entity, String idCitizen) {
        if (entity.equals("house")) {
            long citizenId = Long.parseLong(idCitizen);
            boolean isSuccessOperation = true;
            try {
                deleteAllHousesByCitizenId(citizenId);
            } catch (Exception e) {
                isSuccessOperation = false;
            }
            kafkaTemplate.send("result", "house/delete/house", isSuccessOperation + "/" + citizenId);
        }
    }
}
