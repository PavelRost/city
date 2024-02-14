package org.rostfactory.citizen.client;

import org.rostfactory.citizen.dto.DriverLicenseDtoResponse;

public interface SchoolClient {
    void deleteDriverLicenseByCitizenId(long id);
    DriverLicenseDtoResponse getLicense(long licenseId);
}
