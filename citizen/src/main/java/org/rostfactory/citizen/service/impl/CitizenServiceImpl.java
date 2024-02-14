package org.rostfactory.citizen.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rostfactory.citizen.client.*;
import org.rostfactory.citizen.dto.CitizenDtoResponse;
import org.rostfactory.citizen.entity.Citizen;
import org.rostfactory.citizen.repository.CitizenRepository;
import org.rostfactory.citizen.service.CitizenService;
import org.rostfactory.citizen.service.OcGateway;
import org.rostfactory.sharemodule.dto.EntryDtoResponse;
import org.rostfactory.sharemodule.enums.TypeEntry;
import org.rostfactory.sharemodule.enums.TypeOperationInLog;
import org.rostfactory.sharemodule.enums.TypeWork;
import org.rostfactory.sharemodule.exception.EntityNotFoundException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CitizenServiceImpl implements CitizenService {
    private final CitizenRepository repository;
    private final ReentrantLock locker;
    private final BankClient bankClient;
    private final CarClient carClient;
    private final HouseClient houseClient;
    private final LogClient logClient;
    private final PassportClient passportClient;
    private final SchoolClient schoolClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OcGateway ocGateway;

    @Override
    @Transactional
    public Citizen create(Citizen citizen) {
        locker.lock();
        Citizen citizenEntity = repository.save(citizen);
        try {
            ocGateway.sendEntryInLog(TypeEntry.CITIZEN, TypeOperationInLog.INCREMENT);
        } catch (Exception e) {
        } finally {
            locker.unlock();
        }
        return citizenEntity;
    }

    @Override
    public Citizen update(Citizen citizen) {
        if (!repository.existsByIdAndDeletedFalse(citizen.getId())) throw new EntityNotFoundException();
        return repository.save(citizen);
    }

    @Override
    public void delete(long id) {
        boolean isSuccessOperation = true;
        try {
            Citizen citizen = repository.findCitizenByIdAndDeletedFalse(id).orElseThrow(EntityNotFoundException::new);
            repository.delete(citizen);
            ocGateway.sendEntryInLog(TypeEntry.CITIZEN, TypeOperationInLog.DECREMENT);
        } catch (Exception e) {
            isSuccessOperation = false;
        }
        kafkaTemplate.send("result", "citizen/delete/citizen", isSuccessOperation + "/" + id);
    }

    @Override
    public List<CitizenDtoResponse> findAllCitizen() {
        List<Citizen> citizens = repository.findAllByDeletedFalse();
        return convertCitizenToCitizenDtoResponse(citizens);
    }

    @Override
    public CitizenDtoResponse findById(long id) {
        Citizen citizen = repository.findCitizenByIdAndDeletedFalse(id).orElseThrow(EntityNotFoundException::new);
        return convertCitizenToCitizenDtoResponse(List.of(citizen)).get(0);
    }

    @Override
    public List<CitizenDtoResponse> findByLastNameStartWithLetter(String letter) {
        List<Citizen> citizens = repository.findByLastNameStartWithLetter(letter);
        return convertCitizenToCitizenDtoResponse(citizens);
    }

    private List<CitizenDtoResponse> convertCitizenToCitizenDtoResponse(List<Citizen> citizens) {
        List<CitizenDtoResponse> citizenDtoResponses = new ArrayList<>();
        for (Citizen citizen : citizens) {
            CitizenDtoResponse.CitizenDtoResponseBuilder  builder = CitizenDtoResponse.builder()
                    .id(citizen.getId())
                    .firstName(citizen.getFirstName())
                    .lastName(citizen.getLastName())
                    .gender(citizen.getGender())
                    .job(citizen.getJob())
                    .cars(carClient.getCars(citizen.getId()))
                    .houses(houseClient.getHouses(citizen.getId()));
            if (citizen.getPassportId() != null) {
                builder.passport(passportClient.getPassport(citizen.getPassportId()));
            }
            if (citizen.getAccountBankId() != null) {
                builder.accountBank(bankClient.getAccount(citizen.getAccountBankId()));
            }
            if (citizen.getLicenseId() != null) {
                builder.license(schoolClient.getLicense(citizen.getLicenseId()));
            }
            citizenDtoResponses.add(builder.build());
        }
        return citizenDtoResponses;
    }

    @Override
    @Scheduled(fixedRate = 45, initialDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void citizensRegistrationByTimer() {
        locker.lock();
        try {
            long currentQuantityCitizenBefore = getCurrentQuantityCitizen();
            List<Citizen> citizensBeforeRegistration = createCitizensBeforeRegistration(currentQuantityCitizenBefore);
            List<Citizen> citizensAfterRegistration = new ArrayList<>();
            citizensBeforeRegistration.forEach(citizen -> {
                Citizen citizenEntity = repository.save(citizen);
                ocGateway.sendEntryInLog(TypeEntry.CITIZEN, TypeOperationInLog.INCREMENT);
                citizensAfterRegistration.add(citizenEntity);
            });
            long currentQuantityCitizenAfter = getCurrentQuantityCitizen();
            if (currentQuantityCitizenBefore + citizensBeforeRegistration.size() != currentQuantityCitizenAfter) {
                repository.deleteAll(citizensAfterRegistration);
                IntStream.range(0, 10).forEach(i ->
                        ocGateway.sendEntryInLog(TypeEntry.CITIZEN, TypeOperationInLog.DECREMENT));
            }
        } catch (Exception e) {
        } finally {
            locker.unlock();
        }
    }

    private List<Citizen> createCitizensBeforeRegistration(long currentQuantityCitizen) {
        List<Citizen> citizensBeforeRegistration = new ArrayList<>();
        int quantityCitizensForRegistration = 10;
        long citizenNumber = currentQuantityCitizen + 1;
        for (int i = 0; i < quantityCitizensForRegistration; i++) {
            citizensBeforeRegistration.add(Citizen.builder()
                    .firstName("Имя " + citizenNumber)
                    .lastName("Фамилия " + citizenNumber)
                    .job(TypeWork.FACTORY.name())
                    .build());
            citizenNumber++;
        }
        return citizensBeforeRegistration;
    }

    private Long getCurrentQuantityCitizen() {
        List<EntryDtoResponse> entryEachType = logClient.getEntryEachTypeInLog();
        Optional<EntryDtoResponse> citizenEntry = entryEachType.stream()
                .filter(entry -> entry.getType() == TypeEntry.CITIZEN)
                .findFirst();
        return citizenEntry.isPresent() ? citizenEntry.get().getQuantity() : 0;
    }

    @Override
    @KafkaListener(topics = "event", groupId = "delete")
    public void listener(ConsumerRecord<String, String> record) {
        String[] topicKeys = record.key().split("/");
        String operation = topicKeys[0];
        String entity = topicKeys[1];
        String citizenId = record.value();
        if (operation.equals("rollback")) {
            rollbackOperationByEvent(entity, citizenId);
        }
    }

    private void rollbackOperationByEvent(String entity, String idCitizen) {
        if (entity.equals("citizen")) {
            long citizenId = Long.parseLong(idCitizen);
            boolean isSuccessOperation = true;
            try {
                Citizen citizen = repository.findCitizenByIdAndDeletedTrue(citizenId).orElseThrow(EntityNotFoundException::new);
                citizen.setDeleted(false);
                create(citizen);
            } catch (Exception e) {
                isSuccessOperation = false;
            }
            kafkaTemplate.send("result", "citizen/rollback/citizen", isSuccessOperation + "/" + citizenId);
        }
    }
}
