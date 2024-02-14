package org.rostfactory.police.client.impl;

import lombok.RequiredArgsConstructor;
import org.rostfactory.police.client.SchoolClient;
import org.rostfactory.police.dto.DriverLicenseDtoResponse;
import org.rostfactory.sharemodule.config.UrlConfigProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class SchoolClientImpl implements SchoolClient {
    private final UrlConfigProperties urlConfigProperties;
    private final RestTemplate schoolService;

    @Override
    public DriverLicenseDtoResponse getDriverLicenseByCitizenId(long id) {
        return schoolService.getForObject("%s/findDriverLicenseByCitizenId/%s".formatted(urlConfigProperties.getSchoolServiceUrl(), id), DriverLicenseDtoResponse.class);
    }
}
