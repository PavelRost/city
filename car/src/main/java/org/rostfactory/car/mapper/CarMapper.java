package org.rostfactory.car.mapper;

import org.mapstruct.Mapper;
import org.rostfactory.car.dto.CarDtoRequest;
import org.rostfactory.car.dto.CarDtoRequestCreate;
import org.rostfactory.car.dto.CarDtoResponse;
import org.rostfactory.car.entity.Car;

@Mapper(componentModel = "spring")
public interface CarMapper {
    CarDtoResponse toDto(Car car);
    Car toEntity(CarDtoRequest carDtoRequest);
    Car toEntityForCreate(CarDtoRequestCreate carDtoRequestCreate);
}
