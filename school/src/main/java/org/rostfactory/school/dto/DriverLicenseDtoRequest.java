package org.rostfactory.school.dto;

import lombok.Data;

@Data
public class DriverLicenseDtoRequest {
    private Long id;
    private String value;
    private Long citizenId;
}
