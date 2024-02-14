package org.rostfactory.citizen.client.impl;

import lombok.RequiredArgsConstructor;
import org.rostfactory.citizen.client.HouseClient;
import org.rostfactory.citizen.dto.HouseDtoResponse;
import org.rostfactory.sharemodule.config.UrlConfigProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HouseClientImpl implements HouseClient {
    private final UrlConfigProperties urlConfigProperties;
    private final RestTemplate restTemplate;

    @Override
    public void deleteAllHousesByCitizenId(long id) {
        restTemplate.delete("%s/deleteAllHousesByCitizenId/%s".formatted(urlConfigProperties.getHouseServiceUrl(), id));
    }

    @Override
    public List<HouseDtoResponse> getHouses(long citizenId) {
        ResponseEntity<HouseDtoResponse[]> houses = restTemplate.getForEntity("%s/findHouseByCitizenId/%s".formatted(urlConfigProperties.getHouseServiceUrl(), citizenId), HouseDtoResponse[].class);
        return Arrays.stream(houses.getBody()).toList();
    }
}
