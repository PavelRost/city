package org.rostfactory.car.controller;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.rostfactory.car.dto.CarDtoRequest;
import org.rostfactory.car.dto.CarDtoRequestCreate;
import org.rostfactory.car.dto.CarDtoResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface CarController {
    String CONTROLLER_NAME = "/car-management";

    @ApiResponse(responseCode = "200", description = "",
            content = {@Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CarDtoResponse.class))
            )}
    )
    @GetMapping(CONTROLLER_NAME + "/cars")
    List<CarDtoResponse> findAllCar();

    @ApiResponse(responseCode = "200", description = "",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CarDtoResponse.class)
            )}
    )
    @GetMapping(CONTROLLER_NAME + "/car/{id}")
    CarDtoResponse findCarById(@PathVariable long id);

    @ApiResponse(responseCode = "200", description = "",
            content = {@Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CarDtoResponse.class))
            )}
    )
    @GetMapping(CONTROLLER_NAME + "/cars/owner/{id}")
    List<CarDtoResponse> findCarsByCitizenId(@PathVariable long id);

    @ApiResponse(responseCode = "200", description = "",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CarDtoResponse.class)
            )}
    )
    @GetMapping(CONTROLLER_NAME + "/car/order/{citizenId}")
    CarDtoResponse createPreorderOnCar(@PathVariable long citizenId, @RequestParam long carId);

    @ApiResponse(responseCode = "200", description = "",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CarDtoResponse.class)
            )}
    )
    @PostMapping(CONTROLLER_NAME + "/car")
    CarDtoResponse createCar(@RequestBody CarDtoRequestCreate car);

    @ApiResponse(responseCode = "200", description = "",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CarDtoResponse.class)
            )}
    )
    @PatchMapping(CONTROLLER_NAME + "/car")
    CarDtoResponse updateCar(@RequestBody CarDtoRequest car);

    @DeleteMapping(CONTROLLER_NAME + "/car/{id}")
    void deleteByCarId(@PathVariable long id);

    @DeleteMapping(CONTROLLER_NAME + "/cars/owner/{id}")
    void deleteAllCarsByCitizenId(@PathVariable long id);
}
