package org.rostfactory.school.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PoliceFileDtoRequestCreate {
    private Long citizenId;
    private Long licenseId;
}
