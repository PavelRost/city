package org.rostfactory.police.repository;

import org.rostfactory.police.entity.PoliceFile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PoliceRepository extends CrudRepository<PoliceFile, Long> {
    Optional<PoliceFile> findByCitizenIdAndDeletedFalse(Long id);
    Optional<PoliceFile> findByCitizenIdAndDeletedTrue(Long id);
}
