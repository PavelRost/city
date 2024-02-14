package org.rostfactory.bank.repository;

import org.rostfactory.bank.entity.AccountBank;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BankRepository extends CrudRepository<AccountBank, Long> {
    List<AccountBank> findAllByDeletedFalse();
    Optional<AccountBank> findAccountBankByCitizenIdAndDeletedFalse(Long id);
    Optional<AccountBank> findAccountBankByIdAndDeletedFalse(Long id);
    boolean existsByIdAndDeletedFalse(Long id);
    Optional<AccountBank> findAccountBankByCitizenIdAndDeletedTrue(Long id);
}
