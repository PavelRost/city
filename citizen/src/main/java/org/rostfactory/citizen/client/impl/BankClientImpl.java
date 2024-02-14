package org.rostfactory.citizen.client.impl;


import lombok.RequiredArgsConstructor;
import org.rostfactory.citizen.client.BankClient;
import org.rostfactory.citizen.dto.AccountBankDtoResponse;
import org.rostfactory.sharemodule.config.UrlConfigProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class BankClientImpl implements BankClient {
    private final UrlConfigProperties urlConfigProperties;
    private final RestTemplate restTemplate;

    @Override
    public void deleteAccountBankByCitizenId(long id) {
        restTemplate.delete("%s/deleteByCitizenId/%s".formatted(urlConfigProperties.getBankServiceUrl(), id));
    }

    @Override
    public AccountBankDtoResponse getAccount(long accountBankId) {
        return restTemplate.getForObject("%s/find/%s".formatted(urlConfigProperties.getBankServiceUrl(), accountBankId), AccountBankDtoResponse.class);
    }
}
