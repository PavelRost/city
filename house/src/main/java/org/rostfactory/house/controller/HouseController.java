package org.rostfactory.house.controller;

import lombok.RequiredArgsConstructor;
import org.rostfactory.house.dto.HouseDtoRequest;
import org.rostfactory.house.dto.HouseDtoRequestCreate;
import org.rostfactory.house.dto.HouseDtoResponse;
import org.rostfactory.house.entity.House;
import org.rostfactory.house.mapper.HouseMapper;
import org.rostfactory.house.service.HouseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/house")
public class HouseController {

    private final HouseService service;
    private final HouseMapper houseMapper;

    @GetMapping("/findAll")
    public List<HouseDtoResponse> findAllHouse() {
        return service.findAllHouse().stream()
                .map(houseMapper::toDto)
                .toList();
    }

    @GetMapping("/find/{id}")
    public HouseDtoResponse findHouseById(@PathVariable long id) {
        return houseMapper.toDto(service.findById(id));
    }

    @GetMapping("/findOwnersByHouseAddress")
    public HouseDtoResponse findOwnersByHouseAddress(@RequestParam String address) {
        return houseMapper.toDto(service.findOwnersByHouseAddress(address));
    }

    @GetMapping("/findHouseByCitizenId/{id}")
    public List<HouseDtoResponse> findHouseByCitizenId(@PathVariable long id) {
        return service.findByCitizenId(id).stream()
                .map(houseMapper::toDto)
                .toList();
    }

    @PostMapping("/create")
    public HouseDtoResponse createHouse(@RequestBody HouseDtoRequestCreate house) {
        House houseEntity = houseMapper.toEntityForCreate(house);
        return houseMapper.toDto(service.create(houseEntity));
    }

    @PatchMapping("/update")
    public HouseDtoResponse updateHouse(@RequestBody HouseDtoRequest house) {
        House houseEntity = houseMapper.toEntity(house);
        return houseMapper.toDto(service.update(houseEntity));
    }

    @DeleteMapping("/deleteAllHousesByCitizenId/{id}")
    public void deleteAllHousesByCitizenId(@PathVariable long id) {
        service.deleteAllHousesByCitizenId(id);
    }

    @DeleteMapping("/deleteByHouseId/{id}")
    public void deleteByHouseId(@PathVariable long id) {
        service.deleteByHouseId(id);
    }
}
