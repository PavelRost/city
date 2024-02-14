package org.rostfactory.car.client.impl;

import lombok.RequiredArgsConstructor;
import org.rostfactory.car.client.CitizenClient;
import org.rostfactory.car.dto.CitizenDtoResponse;
import org.rostfactory.sharemodule.config.UrlConfigProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CitizenClientImpl implements CitizenClient {
    private final UrlConfigProperties urlConfigProperties;
    private final RestTemplate restTemplate;

    public List<CitizenDtoResponse> getAllCitizens() {
        ResponseEntity<CitizenDtoResponse[]> citizens =
                restTemplate.getForEntity("%s/findAll".formatted(urlConfigProperties.getCitizenServiceUrl()), CitizenDtoResponse[].class);
        return Arrays.stream(citizens.getBody()).toList();
    }
}
