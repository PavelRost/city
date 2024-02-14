package org.rostfactory.house.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rostfactory.house.entity.House;

import java.util.List;

public interface HouseService {
    House create(House house);
    House update(House house);
    void deleteAllHousesByCitizenId(long id);
    void deleteByHouseId(long id);
    List<House> findAllHouse();
    House findById(long id);
    List<House> findByCitizenId(long citizenId);
    House findOwnersByHouseAddress(String address);
    void listener(ConsumerRecord<String, String> record);
}
