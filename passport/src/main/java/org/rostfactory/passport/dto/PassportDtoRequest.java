package org.rostfactory.passport.dto;

import lombok.Data;

@Data
public class PassportDtoRequest {
    private Long id;
    private String value;
    private Long citizenId;
}
