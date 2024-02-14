package org.rostfactory.log.service;

import org.rostfactory.log.entity.Entry;
import org.rostfactory.sharemodule.enums.TypeEntry;
import org.rostfactory.sharemodule.enums.TypeOperationInLog;

import java.util.List;

public interface LogService {
    Entry create(Entry entry);
    void addEntryLottery(TypeEntry typeEntry);
    void changeEntry(TypeEntry typeEntry, TypeOperationInLog typeOperation);
    List<Entry> getEntryEachType();
    List<Entry> getAllEntryPeriodTime(String startPeriod, String endPeriod);
    void updateLogByTimer();
}
