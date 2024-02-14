package org.rostfactory.citizen.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rostfactory.citizen.dto.CitizenDtoResponse;
import org.rostfactory.citizen.entity.Citizen;

import java.util.List;

public interface CitizenService {
    Citizen create(Citizen citizen);
    Citizen update(Citizen citizen);
    void delete(long id);
    List<CitizenDtoResponse> findAllCitizen();
    CitizenDtoResponse findById(long id);
    List<CitizenDtoResponse> findByLastNameStartWithLetter(String letter);
    void citizensRegistrationByTimer();
    void listener(ConsumerRecord<String, String> record);
}
