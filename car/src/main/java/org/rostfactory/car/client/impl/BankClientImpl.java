package org.rostfactory.car.client.impl;

import lombok.RequiredArgsConstructor;
import org.rostfactory.car.client.BankClient;
import org.rostfactory.sharemodule.config.UrlConfigProperties;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class BankClientImpl implements BankClient {
    private final int COST_CAR = 100;
    private final UrlConfigProperties urlConfigProperties;
    private final RestTemplate restTemplate;

    @Retryable
    public Boolean isEnoughMoney(long citizenId) {
        return restTemplate.getForObject("%s/checkMoneyFromCitizen/%s?purchasePrice=%s".formatted(urlConfigProperties.getBankServiceUrl(), citizenId, COST_CAR), Boolean.class);
    }
}
