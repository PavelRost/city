package org.rostfactory.car.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rostfactory.car.client.BankClient;
import org.rostfactory.car.client.CitizenClient;
import org.rostfactory.car.client.PoliceClient;
import org.rostfactory.car.dto.CitizenDtoResponse;
import org.rostfactory.car.entity.Car;
import org.rostfactory.car.repository.CarRepository;
import org.rostfactory.car.service.CarService;
import org.rostfactory.car.service.ChoosingWinnerService;
import org.rostfactory.car.service.OcGateway;
import org.rostfactory.sharemodule.enums.TypeEntry;
import org.rostfactory.sharemodule.enums.TypeOperationInLog;
import org.rostfactory.sharemodule.exception.EntityNotFoundException;
import org.rostfactory.sharemodule.exception.MoneyOrLicenseNotFoundException;
import org.rostfactory.sharemodule.exception.SellersBusyException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository repository;
    private final ReentrantLock locker;
    private final ChoosingWinnerService choosingWinnerService;
    private final BankClient bankClient;
    private final CitizenClient citizenClient;
    private final PoliceClient policeClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OcGateway ocGateway;

    @Override
    public Car create(Car car) {
        Car carEntry = repository.save(car);
        ocGateway.sendEntryInLog(TypeEntry.CAR, TypeOperationInLog.INCREMENT);
        return carEntry;
    }

    @Override
    public Car createPreorderOnCar(long citizenId, long carId) {
        if (locker.isLocked()) throw new SellersBusyException();
        Car car = findById(carId);
        if (isCanBuyCarThisCitizen(citizenId)) {
            car.setCitizenId(citizenId);
            return repository.save(car);
        }
        throw new MoneyOrLicenseNotFoundException();
    }

    private boolean isCanBuyCarThisCitizen(long citizenId) {
        try {
            boolean isEnoughMoney = bankClient.isEnoughMoney(citizenId);
            boolean isHaveDriverLicense = policeClient.isHaveDriverLicense(citizenId);
            return isEnoughMoney && isHaveDriverLicense;
        } catch (Exception exception) {
        }
        return false;
    }

    @Override
    public Car update(Car car) {
        if (!repository.existsByIdAndDeletedFalse(car.getId())) throw new EntityNotFoundException();
        return repository.save(car);
    }

    @Override
    @Transactional
    public void deleteByCarId(long id) {
        Car car = findById(id);
        repository.delete(car);
        ocGateway.sendEntryInLog(TypeEntry.CAR, TypeOperationInLog.DECREMENT);
    }

    @Override
    public void deleteAllCarsByCitizenId(long id) {
        List<Car> cars = findCarsByCitizenId(id);
        cars.forEach(car -> {
            repository.delete(car);
            ocGateway.sendEntryInLog(TypeEntry.CAR, TypeOperationInLog.DECREMENT);
        });
    }

    @Override
    public List<Car> findAllCar() {
        return repository.findAllByDeletedFalse();
    }

    @Override
    public Car findById(long id) {
        return repository.findCarByIdAndDeletedFalse(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<Car> findCarsByCitizenId(long id) {
        return repository.findCarsByCitizenIdAndDeletedFalse(id);
    }

    @Override
    @Scheduled(fixedRate = 50, initialDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void runningLotteryByTimer() {
        locker.lock();
        try {
            List<CitizenDtoResponse> citizensList = citizenClient.getAllCitizens();
            if (!citizensList.isEmpty()) {
                ocGateway.sendEntryInLog(TypeEntry.LOTTERY, TypeOperationInLog.IGNORE);
                long citizenId = choosingWinnerService.choosingWinner(citizensList).getId();
                Car car = Car.builder()
                        .name("Подарочная машина")
                        .citizenId(citizenId)
                        .build();
                create(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            locker.unlock();
        }
    }

    @Override
    @KafkaListener(topics = "event", groupId = "delete", topicPartitions = {@TopicPartition(topic = "event", partitions = "6")})
    public void listener(ConsumerRecord<String, String> record) {
        String[] topicKeys = record.key().split("/");
        String operation = topicKeys[0];
        String entity = topicKeys[1];
        String citizenId = record.value();
        if (operation.equals("delete")) {
            deleteOperationByEvent(entity, citizenId);
        } else if (operation.equals("rollback")) {
            rollbackOperationByEvent(entity, citizenId);
        }
    }

    private void rollbackOperationByEvent(String entity, String idCitizen) {
        if (entity.equals("car")) {
            long citizenId = Long.parseLong(idCitizen);
            boolean isSuccessOperation = true;
            try {
                List<Car> cars = repository.findCarsByCitizenIdAndDeletedTrue(citizenId);
                cars.forEach(car -> {
                    car.setDeleted(false);
                    create(car);
                });
            } catch (Exception e) {
                isSuccessOperation = false;
            }
            kafkaTemplate.send("result", "car/rollback/car", isSuccessOperation + "/" + citizenId);
        }
    }

    private void deleteOperationByEvent(String entity, String idCitizen) {
        if (entity.equals("car")) {
            long citizenId = Long.parseLong(idCitizen);
            boolean isSuccessOperation = true;
            try {
                deleteAllCarsByCitizenId(citizenId);
            } catch (Exception e) {
                isSuccessOperation = false;
            }
            kafkaTemplate.send("result", "car/delete/car", isSuccessOperation + "/" + citizenId);
        }
    }
}
