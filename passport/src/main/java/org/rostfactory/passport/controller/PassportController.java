package org.rostfactory.passport.controller;

import lombok.RequiredArgsConstructor;
import org.rostfactory.passport.dto.PassportDtoRequest;
import org.rostfactory.passport.dto.PassportDtoRequestCreate;
import org.rostfactory.passport.dto.PassportDtoResponse;
import org.rostfactory.passport.entity.Passport;
import org.rostfactory.passport.mapper.PassportMapper;
import org.rostfactory.passport.service.PassportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passport")
@RequiredArgsConstructor
public class PassportController {

    private final PassportService service;
    private final PassportMapper mapper;

    @GetMapping("/findAll")
    public List<PassportDtoResponse> findAllPassports() {
        return service.findAllPassports().stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/find/{id}")
    public PassportDtoResponse findPassportById(@PathVariable long id) {
        return mapper.toDto(service.findById(id));
    }

    @GetMapping("/findPassportByLastNameStartLetter")
    public List<PassportDtoResponse> findPassportsByLastNameStartLetter(@RequestParam String letter) {
        return service.findPassportsByLastNameStartLetter(letter);
    }

    @PostMapping("/create")
    public PassportDtoResponse createPassport(@RequestBody PassportDtoRequestCreate passport) {
        Passport passportEntity = mapper.toEntityForCreate(passport);
        return mapper.toDto(service.create(passportEntity));
    }

    @PatchMapping("/update")
    public PassportDtoResponse updatePassport(@RequestBody PassportDtoRequest passport) {
        Passport passportEntity = mapper.toEntity(passport);
        return mapper.toDto(service.update(passportEntity));
    }

    @DeleteMapping("/deleteByPassportId/{id}")
    public void deleteByPassportId(@PathVariable long id) {
        service.deleteByPassportId(id);
    }

    @DeleteMapping("/deleteByCitizenId/{id}")
    public void deleteByCitizenId(@PathVariable long id) {
        service.deleteByCitizenId(id);
    }


}
