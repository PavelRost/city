package org.rostfactory.school.controller;

import lombok.RequiredArgsConstructor;
import org.rostfactory.school.dto.DriverLicenseDtoRequest;
import org.rostfactory.school.dto.DriverLicenseDtoRequestCreate;
import org.rostfactory.school.dto.DriverLicenseDtoResponse;
import org.rostfactory.school.entity.DriverLicense;
import org.rostfactory.school.mapper.DriverSchoolMapper;
import org.rostfactory.school.service.DriverSchoolService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/school")
public class DriverSchoolController {

    private final DriverSchoolService service;
    private final DriverSchoolMapper mapper;

    @GetMapping("/findAll")
    public List<DriverLicenseDtoResponse> findAllDriverLicense() {
        return service.findAllDriverLicense().stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/find/{id}")
    public DriverLicenseDtoResponse findDriverLicenseById(@PathVariable long id) {
        return mapper.toDto(service.findById(id));
    }

    @GetMapping("/findDriverLicenseByCitizenId/{id}")
    public DriverLicenseDtoResponse findDriverLicenseByCitizenId(@PathVariable long id) {
        DriverLicense driverLicenseEntity = service.findByCitizenId(id);
        return mapper.toDto(driverLicenseEntity);
    }

    @PostMapping("/create")
    public DriverLicenseDtoResponse createDriverLicense(@RequestBody DriverLicenseDtoRequestCreate license) {
        DriverLicense driverLicenseEntity = mapper.toEntityForCreate(license);
        return mapper.toDto(service.create(driverLicenseEntity));
    }

    @PatchMapping("/update")
    public DriverLicenseDtoResponse updateDriverLicense(@RequestBody DriverLicenseDtoRequest license) {
        DriverLicense driverLicenseEntity = mapper.toEntity(license);
        return mapper.toDto(service.update(driverLicenseEntity));
    }

    @DeleteMapping("/deleteByLicenseId/{id}")
    public void deleteByLicenseId(@PathVariable long id) {
        service.deleteByLicenseId(id);
    }

    @DeleteMapping("/deleteByCitizenId/{id}")
    public void deleteByCitizenId(@PathVariable long id) {
        service.deleteByCitizenId(id);
    }
}
