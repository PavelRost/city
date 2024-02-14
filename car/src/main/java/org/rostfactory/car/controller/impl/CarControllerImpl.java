package org.rostfactory.car.controller.impl;

import lombok.RequiredArgsConstructor;
import org.rostfactory.car.controller.CarController;
import org.rostfactory.car.dto.CarDtoRequest;
import org.rostfactory.car.dto.CarDtoRequestCreate;
import org.rostfactory.car.dto.CarDtoResponse;
import org.rostfactory.car.entity.Car;
import org.rostfactory.car.mapper.CarMapper;
import org.rostfactory.car.service.CarService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CarControllerImpl implements CarController {
    private final CarService service;
    private final CarMapper mapper;

    public List<CarDtoResponse> findAllCar() {
        return service.findAllCar().stream()
                .map(mapper::toDto)
                .toList();
    }

    public CarDtoResponse findCarById(@PathVariable long id) {
        return mapper.toDto(service.findById(id));
    }

    public List<CarDtoResponse> findCarsByCitizenId(@PathVariable long id) {
        return service.findCarsByCitizenId(id).stream()
                .map(mapper::toDto)
                .toList();
    }

    public CarDtoResponse createPreorderOnCar(@PathVariable long citizenId, @RequestParam long carId) {
        return mapper.toDto(service.createPreorderOnCar(citizenId, carId));
    }

    public CarDtoResponse createCar(@RequestBody CarDtoRequestCreate car) {
        Car carEntity = mapper.toEntityForCreate(car);
        return mapper.toDto(service.create(carEntity));
    }

    public CarDtoResponse updateCar(@RequestBody CarDtoRequest car) {
        Car carEntity = mapper.toEntity(car);
        return mapper.toDto(service.update(carEntity));
    }

    public void deleteByCarId(@PathVariable long id) {
        service.deleteByCarId(id);
    }

    public void deleteAllCarsByCitizenId(@PathVariable long id) {
        service.deleteAllCarsByCitizenId(id);
    }
}
