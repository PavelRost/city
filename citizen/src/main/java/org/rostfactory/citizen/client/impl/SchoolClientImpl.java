package org.rostfactory.citizen.client.impl;

import lombok.RequiredArgsConstructor;
import org.rostfactory.citizen.client.SchoolClient;
import org.rostfactory.citizen.dto.DriverLicenseDtoResponse;
import org.rostfactory.sharemodule.config.UrlConfigProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class SchoolClientImpl implements SchoolClient {
    private final UrlConfigProperties urlConfigProperties;
    private final RestTemplate restTemplate;


    @Override
    public void deleteDriverLicenseByCitizenId(long id) {
        restTemplate.delete("%s/deleteByCitizenId/%s".formatted(urlConfigProperties.getSchoolServiceUrl(), id));
    }

    @Override
    public DriverLicenseDtoResponse getLicense(long licenseId) {
        return restTemplate.getForObject("%s/find/%s".formatted(urlConfigProperties.getSchoolServiceUrl(), licenseId), DriverLicenseDtoResponse.class);
    }
}
