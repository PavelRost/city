package org.rostfactory.citizen.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CitizenDtoResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private String job;
    private PassportDtoResponse passport;
    private DriverLicenseDtoResponse license;
    private AccountBankDtoResponse accountBank;
    private List<CarDtoResponse> cars;
    private List<HouseDtoResponse> houses;
}
