package org.rostfactory.school.repository;

import org.rostfactory.school.entity.DriverLicense;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DriverSchoolRepository extends CrudRepository<DriverLicense, Long> {
    List<DriverLicense> findAllByDeletedFalse();
    Optional<DriverLicense> findDriverLicenseByCitizenIdAndDeletedFalse(Long id);
    Optional<DriverLicense> findDriverLicenseByCitizenIdAndDeletedTrue(Long id);
    Optional<DriverLicense> findDriverLicenseByIdAndDeletedFalse(Long id);
    boolean existsByIdAndDeletedFalse(Long id);
}
