package org.rostfactory.log.repository;

import org.rostfactory.log.entity.Entry;
import org.rostfactory.sharemodule.enums.TypeEntry;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LogRepository extends CrudRepository<Entry, Long> {
    List<Entry> findAllByDateTimeBetween(LocalDateTime startPeriod, LocalDateTime endPeriod);
    Optional<Entry> findTopByTypeOrderByDateTimeDesc(TypeEntry typeEntry);
    List<Entry> findAll();
}
