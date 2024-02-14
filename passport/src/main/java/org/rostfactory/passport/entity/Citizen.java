package org.rostfactory.passport.entity;

import lombok.Data;
import org.rostfactory.passport.dto.PassportDtoResponse;

@Data
public class Citizen {
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private PassportDtoResponse passport;
}
