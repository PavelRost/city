package org.rostfactory.car.repository;

import org.rostfactory.car.entity.Car;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends CrudRepository<Car, Long> {
    List<Car> findAllByDeletedFalse();
    List<Car> findCarsByCitizenIdAndDeletedFalse(Long id);
    List<Car> findCarsByCitizenIdAndDeletedTrue(Long id);
    Optional<Car> findCarByIdAndDeletedFalse(Long id);
    boolean existsByIdAndDeletedFalse(Long id);
}
