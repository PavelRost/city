package org.rostfactory.passport.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rostfactory.passport.dto.PassportDtoResponse;
import org.rostfactory.passport.entity.Passport;

import java.util.List;

public interface PassportService {
    Passport create(Passport passport);
    Passport update(Passport passportNew);
    void deleteByPassportId(long id);
    void deleteByCitizenId(long id);
    List<Passport> findAllPassports();
    Passport findById(long id);
    Passport findByCitizenId(long id);
    List<PassportDtoResponse> findPassportsByLastNameStartLetter(String letter);
    void listener(ConsumerRecord<String, String> record);
}
