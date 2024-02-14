package org.rostfactory.passport.mapper;

import org.mapstruct.Mapper;
import org.rostfactory.passport.dto.PassportDtoRequest;
import org.rostfactory.passport.dto.PassportDtoRequestCreate;
import org.rostfactory.passport.dto.PassportDtoResponse;
import org.rostfactory.passport.entity.Passport;

@Mapper(componentModel = "spring")
public interface PassportMapper {
    PassportDtoResponse toDto(Passport passport);
    Passport toEntity(PassportDtoRequest passportDtoRequest);
    Passport toEntityForCreate(PassportDtoRequestCreate passportDtoRequestCreate);
}
