package org.rostfactory.citizen.controller;

import lombok.RequiredArgsConstructor;
import org.rostfactory.citizen.dto.CitizenDtoRequest;
import org.rostfactory.citizen.dto.CitizenDtoRequestCreate;
import org.rostfactory.citizen.dto.CitizenDtoResponse;
import org.rostfactory.citizen.entity.Citizen;
import org.rostfactory.citizen.mapper.CitizenMapper;
import org.rostfactory.citizen.service.CitizenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/citizen")
@RequiredArgsConstructor
public class CitizenController {

    private final CitizenService service;
    private final CitizenMapper citizenMapper;

    @GetMapping("/findAll")
    public List<CitizenDtoResponse> findAllCitizens() {
        return service.findAllCitizen();
    }

    @GetMapping("/find/{id}")
    public CitizenDtoResponse findCitizenById(@PathVariable int id) {
        return service.findById(id);
    }

    @GetMapping("/findByLastNameStartWithLetter")
    public List<CitizenDtoResponse> findByLastNameStartWithLetter(@RequestParam String letter) {
        return service.findByLastNameStartWithLetter(letter);
    }

    @PostMapping("/create")
    public CitizenDtoResponse createCitizen(@RequestBody CitizenDtoRequestCreate citizen) {
        Citizen citizenEntity = citizenMapper.toEntityForCreate(citizen);
        return citizenMapper.toDto(service.create(citizenEntity));
    }

    @PatchMapping("/update")
    public CitizenDtoResponse updateCitizen(@RequestBody CitizenDtoRequest citizenNew) {
        Citizen citizenEntity = citizenMapper.toEntity(citizenNew);
        return citizenMapper.toDto(service.update(citizenEntity));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCitizen(@PathVariable int id) {
        service.delete(id);
    }
}
