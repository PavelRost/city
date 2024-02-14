package org.rostfactory.citizen.client;

import org.rostfactory.citizen.dto.HouseDtoResponse;

import java.util.List;

public interface HouseClient {
    void deleteAllHousesByCitizenId(long id);
    List<HouseDtoResponse> getHouses(long citizenId);
}
