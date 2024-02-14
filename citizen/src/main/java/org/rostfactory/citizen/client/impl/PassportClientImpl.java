package org.rostfactory.citizen.client.impl;

import lombok.RequiredArgsConstructor;
import org.rostfactory.citizen.client.PassportClient;
import org.rostfactory.citizen.dto.PassportDtoResponse;
import org.rostfactory.sharemodule.config.UrlConfigProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PassportClientImpl implements PassportClient {
    private final UrlConfigProperties urlConfigProperties;
    private final RestTemplate restTemplate;

    @Override
    public void deletePassportByCitizenId(long id) {
        restTemplate.delete("%s/deleteByCitizenId/%s".formatted(urlConfigProperties.getPassportServiceUrl(), id));
    }

    @Override
    public PassportDtoResponse getPassport(long passportId) {
        return restTemplate.getForObject("%s/find/%s".formatted(urlConfigProperties.getPassportServiceUrl(), passportId), PassportDtoResponse.class);
    }
}
