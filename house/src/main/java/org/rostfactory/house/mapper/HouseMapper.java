package org.rostfactory.house.mapper;

import org.mapstruct.Mapper;
import org.rostfactory.house.dto.HouseDtoRequest;
import org.rostfactory.house.dto.HouseDtoRequestCreate;
import org.rostfactory.house.dto.HouseDtoResponse;
import org.rostfactory.house.entity.House;

@Mapper(componentModel = "spring")
public interface HouseMapper {
    HouseDtoResponse toDto(House house);
    House toEntity(HouseDtoRequest houseDtoRequest);
    House toEntityForCreate(HouseDtoRequestCreate houseDtoRequestCreate);
}
