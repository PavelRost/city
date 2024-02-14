package org.rostfactory.citizen.mapper;

import org.mapstruct.Mapper;
import org.rostfactory.citizen.dto.CitizenDtoRequest;
import org.rostfactory.citizen.dto.CitizenDtoRequestCreate;
import org.rostfactory.citizen.dto.CitizenDtoResponse;
import org.rostfactory.citizen.entity.Citizen;

@Mapper(componentModel = "spring")
public interface CitizenMapper {
    CitizenDtoResponse toDto(Citizen citizen);
    Citizen toEntity(CitizenDtoRequest citizenDtoRequest);
    Citizen toEntityForCreate(CitizenDtoRequestCreate citizenDtoRequestCreate);
}
