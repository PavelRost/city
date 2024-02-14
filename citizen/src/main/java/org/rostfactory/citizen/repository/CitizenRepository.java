package org.rostfactory.citizen.repository;

import org.rostfactory.citizen.entity.Citizen;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CitizenRepository extends CrudRepository<Citizen, Long> {
    List<Citizen> findAllByDeletedFalse();
    @Query(value = "SELECT * FROM citizen WHERE last_name LIKE ?1% AND deleted = false", nativeQuery = true)
    List<Citizen> findByLastNameStartWithLetter(String letter);
    Optional<Citizen> findCitizenByIdAndDeletedFalse(Long id);
    Optional<Citizen> findCitizenByIdAndDeletedTrue(Long id);
    boolean existsByIdAndDeletedFalse(Long id);
}
