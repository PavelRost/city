package org.rostfactory.log.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.rostfactory.log.entity.Entry;
import org.rostfactory.log.repository.LogRepository;
import org.rostfactory.log.service.LogService;
import org.rostfactory.sharemodule.enums.TypeEntry;
import org.rostfactory.sharemodule.enums.TypeOperationInLog;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository repository;
    private static final AtomicLong accountCount = new AtomicLong(0);
    private static final AtomicLong carCount = new AtomicLong(0);
    private static final AtomicLong citizenCount = new AtomicLong(0);
    private static final AtomicLong houseCount = new AtomicLong(0);
    private static final AtomicLong passportCount = new AtomicLong(0);
    private static final AtomicLong licenseCount = new AtomicLong(0);

    @PostConstruct
    private void init() {
        if (repository.findAll().isEmpty()) {
            updateLogByTimer();
            return;
        }
        repository.findTopByTypeOrderByDateTimeDesc(TypeEntry.ACCOUNT).ifPresent(entry -> accountCount.set(entry.getQuantity()));
        repository.findTopByTypeOrderByDateTimeDesc(TypeEntry.CAR).ifPresent(entry -> carCount.set(entry.getQuantity()));
        repository.findTopByTypeOrderByDateTimeDesc(TypeEntry.CITIZEN).ifPresent(entry -> citizenCount.set(entry.getQuantity()));
        repository.findTopByTypeOrderByDateTimeDesc(TypeEntry.HOUSE).ifPresent(entry -> houseCount.set(entry.getQuantity()));
        repository.findTopByTypeOrderByDateTimeDesc(TypeEntry.PASSPORT).ifPresent(entry -> passportCount.set(entry.getQuantity()));
        repository.findTopByTypeOrderByDateTimeDesc(TypeEntry.LICENSE).ifPresent(entry -> licenseCount.set(entry.getQuantity()));
    }

    @Override
    public Entry create(Entry entry) {
        return repository.save(entry);
    }

    @Override
    public void addEntryLottery(TypeEntry typeEntry) {
        LocalDateTime dateTime = LocalDateTime.now();
        Entry entryLottery = Entry.builder()
                .type(typeEntry)
                .quantity(0L)
                .dateTime(dateTime)
                .build();
        create(entryLottery);
    }

    @Override
    public void changeEntry(TypeEntry typeEntry, TypeOperationInLog typeOperation) {
        if (typeOperation.equals(TypeOperationInLog.INCREMENT)) {
            switch (typeEntry) {
                case ACCOUNT -> accountCount.incrementAndGet();
                case CAR -> carCount.incrementAndGet();
                case CITIZEN -> citizenCount.incrementAndGet();
                case HOUSE -> houseCount.incrementAndGet();
                case PASSPORT -> passportCount.incrementAndGet();
                case LICENSE -> licenseCount.incrementAndGet();
            }
        } else if (typeOperation.equals(TypeOperationInLog.DECREMENT)) {
            switch (typeEntry) {
                case ACCOUNT -> accountCount.decrementAndGet();
                case CAR -> carCount.decrementAndGet();
                case CITIZEN -> citizenCount.decrementAndGet();
                case HOUSE -> houseCount.decrementAndGet();
                case PASSPORT -> passportCount.decrementAndGet();
                case LICENSE -> licenseCount.decrementAndGet();
            }
        }
    }

    @Override
    public List<Entry> getEntryEachType() {
        return createEntryList();
    }

    @Override
    public List<Entry> getAllEntryPeriodTime(String startPeriod, String endPeriod) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startPeriod, formatter);
        LocalDateTime end = LocalDateTime.parse(endPeriod, formatter);
        return repository.findAllByDateTimeBetween(start, end);
    }

    @Override
    @Scheduled(fixedRate = 1, initialDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void updateLogByTimer() {
        repository.saveAll(createEntryList());
    }

    private List<Entry> createEntryList() {
        LocalDateTime dateTime = LocalDateTime.now();
        List<Entry> entries = new ArrayList<>();
        entries.add(Entry.builder()
                .type(TypeEntry.ACCOUNT)
                .quantity(accountCount.get())
                .dateTime(dateTime)
                .build());
        entries.add(Entry.builder()
                .type(TypeEntry.CAR)
                .quantity(carCount.get())
                .dateTime(dateTime)
                .build());
        entries.add(Entry.builder()
                .type(TypeEntry.CITIZEN)
                .quantity(citizenCount.get())
                .dateTime(dateTime)
                .build());
        entries.add(Entry.builder()
                .type(TypeEntry.HOUSE)
                .quantity(houseCount.get())
                .dateTime(dateTime)
                .build());
        entries.add(Entry.builder()
                .type(TypeEntry.PASSPORT)
                .quantity(passportCount.get())
                .dateTime(dateTime)
                .build());
        entries.add(Entry.builder()
                .type(TypeEntry.LICENSE)
                .quantity(licenseCount.get())
                .dateTime(dateTime)
                .build());
        return entries;
    }
}
