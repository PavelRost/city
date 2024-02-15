package org.rostfactory.school.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverLicenseDtoRequestCreate {
    private Long citizenId;
    private String value;
}
