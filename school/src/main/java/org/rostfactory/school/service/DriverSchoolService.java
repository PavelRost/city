package org.rostfactory.school.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rostfactory.school.entity.DriverLicense;

import java.util.List;

public interface DriverSchoolService {
    List<DriverLicense> findAllDriverLicense();
    DriverLicense findById(long id);
    DriverLicense findByCitizenId(long id);
    DriverLicense create(DriverLicense license);
    DriverLicense update(DriverLicense license);
    void deleteByCitizenId(long id);
    void deleteByLicenseId(long id);
    void listener(ConsumerRecord<String, String> record);
}
