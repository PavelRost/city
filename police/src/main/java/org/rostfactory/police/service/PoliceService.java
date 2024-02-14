package org.rostfactory.police.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rostfactory.police.entity.PoliceFile;

public interface PoliceService {
    boolean isExistDriverLicenseByCitizenId(long id);
    PoliceFile create(PoliceFile file);
    void delete(long citizenId);
    PoliceFile findByCitizenId(long id);
    void listener(ConsumerRecord<String, String> record);
}
