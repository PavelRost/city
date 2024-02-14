package org.rostfactory.car.client.impl;

import lombok.RequiredArgsConstructor;
import org.rostfactory.car.client.PoliceClient;
import org.rostfactory.sharemodule.config.UrlConfigProperties;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PoliceClientImpl implements PoliceClient {
    private final UrlConfigProperties urlConfigProperties;
    private final RestTemplate restTemplate;

    @Retryable
    public Boolean isHaveDriverLicense(long citizenId) {
        return restTemplate.getForObject("%s/isExistDriverLicenseByCitizenId/%s".formatted(urlConfigProperties.getPoliceServiceUrl(), citizenId), Boolean.class);
    }
}
