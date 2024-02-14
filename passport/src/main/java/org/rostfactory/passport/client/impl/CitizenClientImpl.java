package org.rostfactory.passport.client.impl;

import lombok.RequiredArgsConstructor;
import org.rostfactory.passport.client.CitizenClient;
import org.rostfactory.passport.dto.CitizenDtoResponse;
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

    @Override
    public List<CitizenDtoResponse> findCitizensByLastNameStartLetter(String letter) {
        ResponseEntity<CitizenDtoResponse[]> citizens = restTemplate.getForEntity("%s/findByLastNameStartWithLetter?letter=%s".formatted(urlConfigProperties.getCitizenServiceUrl(), letter), CitizenDtoResponse[].class);
        return Arrays.stream(citizens.getBody()).toList();
    }
}
