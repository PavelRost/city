package org.rostfactory.bank.dto;

import lombok.Data;

@Data
public class AccountBankDtoRequest {
    private Long id;
    private long money;
    private Long citizenId;
}
