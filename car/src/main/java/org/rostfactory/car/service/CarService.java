package org.rostfactory.car.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.rostfactory.car.entity.Car;

import java.util.List;

public interface CarService {
    Car create(Car car);
    Car createPreorderOnCar(long citizenId, long carId);
    Car update(Car Car);
    void deleteByCarId(long id);
    void deleteAllCarsByCitizenId(long id);
    List<Car> findAllCar();
    Car findById(long id);
    List<Car> findCarsByCitizenId(long id);
    void runningLotteryByTimer();
    void listener(ConsumerRecord<String, String> record);
}
