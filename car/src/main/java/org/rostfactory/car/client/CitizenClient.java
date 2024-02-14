package org.rostfactory.car.client;

import org.rostfactory.car.dto.CitizenDtoResponse;

import java.util.List;

public interface CitizenClient {
    List<CitizenDtoResponse> getAllCitizens();
}
