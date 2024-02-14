package org.rostfactory.bank.mapper;

import org.mapstruct.Mapper;
import org.rostfactory.bank.dto.AccountBankDtoRequest;
import org.rostfactory.bank.dto.AccountBankDtoRequestCreate;
import org.rostfactory.bank.dto.AccountBankDtoResponse;
import org.rostfactory.bank.entity.AccountBank;

@Mapper(componentModel = "spring")
public interface BankMapper {
    AccountBankDtoResponse toDto(AccountBank accountBank);
    AccountBank toEntity(AccountBankDtoRequest AccountBankDtoRequest);
    AccountBank toEntityForCreate(AccountBankDtoRequestCreate accountBankDtoRequestCreate);
}
