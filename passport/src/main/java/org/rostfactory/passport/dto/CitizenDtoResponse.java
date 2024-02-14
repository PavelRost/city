package org.rostfactory.passport.dto;

import lombok.Data;

import java.util.List;

@Data
public class CitizenDtoResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private PassportDtoResponse passport;
    private DriverLicenseDtoResponse license;
    private AccountBankDtoResponse accountBank;
    private List<CarDtoResponse> cars;
    private List<HouseDtoResponse> houses;
}
