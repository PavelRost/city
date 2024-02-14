package org.rostfactory.citizen.client.impl;

import lombok.RequiredArgsConstructor;
import org.rostfactory.citizen.client.CarClient;
import org.rostfactory.citizen.dto.CarDtoResponse;
import org.rostfactory.sharemodule.config.UrlConfigProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CarClientImpl implements CarClient {
    private final UrlConfigProperties urlConfigProperties;
    private final RestTemplate restTemplate;

    @Override
    public List<CarDtoResponse> getCars(long citizenId) {
        ResponseEntity<CarDtoResponse[]> cars = restTemplate.getForEntity("%s/cars/owner/%s".formatted(urlConfigProperties.getCarServiceUrl(), citizenId), CarDtoResponse[].class);
        return Arrays.stream(cars.getBody()).toList();
    }

    @Override
    public void deleteAllCarsByCitizenId(long id) {
        restTemplate.delete("%s/cars/owner/%s".formatted(urlConfigProperties.getCarServiceUrl(), id));
    }
}
