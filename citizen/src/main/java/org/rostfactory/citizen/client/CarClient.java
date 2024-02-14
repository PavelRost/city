package org.rostfactory.citizen.client;

import org.rostfactory.citizen.dto.CarDtoResponse;

import java.util.List;

public interface CarClient {
    List<CarDtoResponse> getCars(long citizenId);
    void deleteAllCarsByCitizenId(long id);
}
