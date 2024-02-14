package org.rostfactory.police.controller;

import lombok.RequiredArgsConstructor;
import org.rostfactory.police.dto.PoliceFileDtoRequestCreate;
import org.rostfactory.police.dto.PoliceFileDtoResponse;
import org.rostfactory.police.entity.PoliceFile;
import org.rostfactory.police.mapper.PoliceFileMapper;
import org.rostfactory.police.service.PoliceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/police")
public class PoliceController {

    private final PoliceService service;
    private final PoliceFileMapper mapper;

    @GetMapping("/isExistDriverLicenseByCitizenId/{id}")
    public Boolean isExistDriverLicenseByCitizenId(@PathVariable long id) {
        return service.isExistDriverLicenseByCitizenId(id);
    }

    @PostMapping("/create")
    public PoliceFileDtoResponse createPoliceFile(@RequestBody PoliceFileDtoRequestCreate policeFile) {
        PoliceFile policeFileEntity = mapper.toEntityForCreate(policeFile);
        return mapper.toDto(service.create(policeFileEntity));
    }

    @DeleteMapping("/deleteByCitizenId/{id}")
    public void deletePoliceFileByCitizenId(@PathVariable long id) {
        service.delete(id);
    }
}
