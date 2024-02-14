package org.rostfactory.police.client;

import org.rostfactory.police.dto.DriverLicenseDtoResponse;

public interface SchoolClient {
    DriverLicenseDtoResponse getDriverLicenseByCitizenId(long id);
}
