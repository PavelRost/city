package org.rostfactory.citizen.dto;

import lombok.Data;

@Data
public class CitizenDtoRequestCreate {
    private String firstName;
    private String lastName;
    private String gender;
    private String job;
    private Long passportId;
    private Long licenseId;
    private Long accountBankId;
}
