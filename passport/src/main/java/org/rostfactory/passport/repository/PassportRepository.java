package org.rostfactory.passport.repository;

import org.rostfactory.passport.entity.Passport;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PassportRepository extends CrudRepository<Passport, Long> {
    List<Passport> findAllByDeletedFalse();
    Optional<Passport> findPassportByCitizenIdAndDeletedFalse(Long id);
    Optional<Passport> findPassportByCitizenIdAndDeletedTrue(Long id);
    Optional<Passport> findPassportByIdAndDeletedFalse(Long id);
    boolean existsByIdAndDeletedFalse(Long id);
}
