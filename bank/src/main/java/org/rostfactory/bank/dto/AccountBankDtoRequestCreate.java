package org.rostfactory.bank.dto;

import lombok.Data;

@Data
public class AccountBankDtoRequestCreate {
    private Long citizenId;
    private long money;
}
