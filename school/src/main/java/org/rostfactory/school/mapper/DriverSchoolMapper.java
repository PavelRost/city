package org.rostfactory.school.mapper;

import org.mapstruct.Mapper;
import org.rostfactory.school.dto.DriverLicenseDtoRequest;
import org.rostfactory.school.dto.DriverLicenseDtoRequestCreate;
import org.rostfactory.school.dto.DriverLicenseDtoResponse;
import org.rostfactory.school.entity.DriverLicense;

@Mapper(componentModel = "spring")
public interface DriverSchoolMapper {
    DriverLicenseDtoResponse toDto(DriverLicense driverLicense);
    DriverLicense toEntity(DriverLicenseDtoRequest driverLicenseDtoRequest);
    DriverLicense toEntityForCreate(DriverLicenseDtoRequestCreate driverLicenseDtoRequestCreate);
}
