package org.rostfactory.school.client;

import org.rostfactory.school.entity.DriverLicense;

public interface PoliceClient {
    void createAndSendPoliceFile(DriverLicense license);
    void deletePoliceFileByCitizenId(long id);
}
