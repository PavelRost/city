package org.rostfactory.bank.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rostfactory.bank.entity.AccountBank;

import java.util.List;

public interface BankService {
    List<AccountBank> findAllAccount();
    AccountBank findById(long id);
    AccountBank findByCitizenId(long id);
    boolean isEnoughMoneyForPurchaseByCitizenId(long citizenId, int purchasePrice);
    AccountBank create(AccountBank account);
    AccountBank update(AccountBank account);
    void deleteByAccountBankId(long id);
    void deleteByCitizenId(long citizenId);
    void listener(ConsumerRecord<String, String> record);
}
