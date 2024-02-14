package org.rostfactory.bank.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rostfactory.bank.entity.AccountBank;
import org.rostfactory.bank.repository.BankRepository;
import org.rostfactory.bank.service.BankService;
import org.rostfactory.bank.service.OcGateway;
import org.rostfactory.sharemodule.enums.TypeEntry;
import org.rostfactory.sharemodule.enums.TypeOperationInLog;
import org.rostfactory.sharemodule.exception.EntityNotFoundException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OcGateway ocGateway;

    @Override
    public List<AccountBank> findAllAccount() {
        return repository.findAllByDeletedFalse();
    }

    @Override
    public AccountBank findById(long id) {
        return repository.findAccountBankByIdAndDeletedFalse(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public AccountBank findByCitizenId(long id) {
        return repository.findAccountBankByCitizenIdAndDeletedFalse(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public boolean isEnoughMoneyForPurchaseByCitizenId(long citizenId, int purchasePrice) {
        AccountBank account = findByCitizenId(citizenId);
        return account.getMoney() >= purchasePrice;
    }

    @Override
    public AccountBank create(AccountBank account) {
        AccountBank accountBank = repository.save(account);
        ocGateway.sendEntryInLog(TypeEntry.ACCOUNT, TypeOperationInLog.INCREMENT);
        return accountBank;
    }

    @Override
    public AccountBank update(AccountBank account) {
        if (!repository.existsByIdAndDeletedFalse(account.getId())) throw new EntityNotFoundException();
        return repository.save(account);
    }

    @Override
    public void deleteByAccountBankId(long id) {
        AccountBank account = findById(id);
        repository.delete(account);
        ocGateway.sendEntryInLog(TypeEntry.ACCOUNT, TypeOperationInLog.DECREMENT);
    }

    @Override
    public void deleteByCitizenId(long citizenId) {
        AccountBank account = findByCitizenId(citizenId);
        repository.delete(account);
        ocGateway.sendEntryInLog(TypeEntry.ACCOUNT, TypeOperationInLog.DECREMENT);
    }

    @Override
    @KafkaListener(topics = "event", groupId = "delete", topicPartitions = {@TopicPartition(topic = "event", partitions = "1")})
    public void listener(ConsumerRecord<String, String> record) {
        String[] topicKeys = record.key().split("/");
        String operation = topicKeys[0];
        String entity = topicKeys[1];
        String citizenId = record.value();
        if (operation.equals("delete")) {
            deleteOperationByEvent(entity, citizenId);
        } else if (operation.equals("rollback")) {
            rollbackOperationByEvent(entity, citizenId);
        }
    }

    private void rollbackOperationByEvent(String entity, String idCitizen) {
        if (entity.equals("account")) {
            long citizenId = Long.parseLong(idCitizen);
            boolean isSuccessOperation = true;
            try {
                AccountBank accountBank = repository.findAccountBankByCitizenIdAndDeletedTrue(citizenId)
                        .orElseThrow(EntityNotFoundException::new);
                accountBank.setDeleted(false);
                create(accountBank);
            } catch (Exception e) {
                isSuccessOperation = false;
            }
            kafkaTemplate.send("result", "bank/rollback/account", isSuccessOperation + "/" + citizenId);
        }
    }

    private void deleteOperationByEvent(String entity, String idCitizen) {
        if (entity.equals("account")) {
            long citizenId = Long.parseLong(idCitizen);
            boolean isSuccessOperation = true;
            try {
                deleteByCitizenId(citizenId);
            } catch (Exception e) {
                isSuccessOperation = false;
            }
            kafkaTemplate.send("result", "bank/delete/account", isSuccessOperation + "/" + citizenId);
        }
    }
}
