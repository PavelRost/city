package org.rostfactory.school.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverLicenseDtoRequest {
    private Long id;
    private String value;
    private Long citizenId;
}
