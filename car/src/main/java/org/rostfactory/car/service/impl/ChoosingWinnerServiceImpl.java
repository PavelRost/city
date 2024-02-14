package org.rostfactory.car.service.impl;

import lombok.RequiredArgsConstructor;
import org.rostfactory.car.dto.CitizenDtoResponse;
import org.rostfactory.car.service.ChoosingWinnerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ChoosingWinnerServiceImpl implements ChoosingWinnerService {

    @Override
    public CitizenDtoResponse choosingWinner(List<CitizenDtoResponse> citizensList) {
        int winnerNumber = new Random().nextInt(citizensList.size() - 1);
        return citizensList.get(winnerNumber);
    }
}
