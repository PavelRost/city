package org.rostfactory.car.service;

import org.rostfactory.car.dto.CitizenDtoResponse;

import java.util.List;

public interface ChoosingWinnerService {
    CitizenDtoResponse choosingWinner(List<CitizenDtoResponse> citizensList);
}
