package org.rostfactory.house.dto;

import lombok.Data;

@Data
public class HouseDtoRequest {
    private Long id;
    private Long citizenId;
    private String address;
}
