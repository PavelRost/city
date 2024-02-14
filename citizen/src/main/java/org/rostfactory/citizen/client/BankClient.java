package org.rostfactory.citizen.client;

import org.rostfactory.citizen.dto.AccountBankDtoResponse;

public interface BankClient {
    void deleteAccountBankByCitizenId(long id);
    AccountBankDtoResponse getAccount(long accountBankId);
}
