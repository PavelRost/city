package org.rostfactory.log.controller;

import lombok.RequiredArgsConstructor;
import org.rostfactory.log.mapper.EntryMapper;
import org.rostfactory.log.service.LogService;
import org.rostfactory.sharemodule.dto.EntryChangeDtoRequest;
import org.rostfactory.sharemodule.dto.EntryDtoResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;
    private final EntryMapper mapper;

    @PostMapping("/changeEntry")
    public void changeEntry(@RequestBody EntryChangeDtoRequest entryChangeDtoRequest) {
        logService.changeEntry(entryChangeDtoRequest.getTypeEntry(), entryChangeDtoRequest.getTypeOperation());
    }

    @GetMapping("/getEntryEachType")
    public List<EntryDtoResponse> getEntryEachType() {
        return logService.getEntryEachType().stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/getAllEntryPeriodTime")
    public List<EntryDtoResponse> getAllEntryPeriodTime(@RequestParam String startPeriod, @RequestParam String endPeriod) {
        return logService.getAllEntryPeriodTime(startPeriod, endPeriod).stream()
                .map(mapper::toDto)
                .toList();
    }

    @MessageMapping("/entry")
    public void changeEntryByWebSocket(EntryChangeDtoRequest entryChangeDtoRequest) {
        logService.changeEntry(entryChangeDtoRequest.getTypeEntry(), entryChangeDtoRequest.getTypeOperation());
    }

    @MessageMapping("/lottery")
    public void lotteryLogByWebSocket(EntryChangeDtoRequest entryChangeDtoRequest) {
        logService.addEntryLottery(entryChangeDtoRequest.getTypeEntry());
    }
}
