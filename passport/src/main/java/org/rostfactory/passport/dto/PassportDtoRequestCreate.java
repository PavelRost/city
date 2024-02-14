package org.rostfactory.passport.dto;

import lombok.Data;

@Data
public class PassportDtoRequestCreate {
    private Long citizenId;
    private String value;
}
