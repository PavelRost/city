package org.rostfactory.school.client.impl;

import lombok.RequiredArgsConstructor;
import org.rostfactory.school.client.PoliceClient;
import org.rostfactory.school.dto.PoliceFileDtoRequestCreate;
import org.rostfactory.school.dto.PoliceFileDtoResponse;
import org.rostfactory.school.entity.DriverLicense;
import org.rostfactory.sharemodule.config.UrlConfigProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PoliceClientImpl implements PoliceClient {
    private final UrlConfigProperties urlConfigProperties;
    private final RestTemplate restTemplate;

    @Override
    public void createAndSendPoliceFile(DriverLicense license) {
        restTemplate.postForObject(
                urlConfigProperties.getPoliceServiceUrl() + "/create",
                PoliceFileDtoRequestCreate.builder().
                        licenseId(license.getId()).citizenId(license.getCitizenId())
                        .build(),
                PoliceFileDtoResponse.class);
    }

    @Override
    public void deletePoliceFileByCitizenId(long id) {
        restTemplate.delete("%s/deleteByCitizenId/%s".formatted(urlConfigProperties.getPoliceServiceUrl(), id));
    }
}
