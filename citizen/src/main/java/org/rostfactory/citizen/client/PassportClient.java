package org.rostfactory.citizen.client;

import org.rostfactory.citizen.dto.PassportDtoResponse;

public interface PassportClient {
    void deletePassportByCitizenId(long id);
    PassportDtoResponse getPassport(long passportId);
}
