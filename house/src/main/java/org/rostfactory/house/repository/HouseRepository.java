package org.rostfactory.house.repository;

import org.rostfactory.house.entity.House;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HouseRepository extends CrudRepository<House, Long> {
    List<House> findAllByDeletedFalse();
    @Query("from House h where h.address = :address and h.deleted = false")
    Optional<House> findAllByAddress(@Param("address") String address);
    List<House> findHouseByCitizenIdAndDeletedFalse(Long id);
    List<House> findHouseByCitizenIdAndDeletedTrue(Long id);
    Optional<House> findByIdAndDeletedFalse(Long id);
    boolean existsByIdAndDeletedFalse(Long id);
}
