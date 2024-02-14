package org.rostfactory.police.dto;

import lombok.Data;

@Data
public class PoliceFileDtoRequestCreate {
    private Long citizenId;
    private Long licenseId;
}
