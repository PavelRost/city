package org.rostfactory.starter.service;

import org.rostfactory.sharemodule.enums.TypeEntry;
import org.rostfactory.sharemodule.enums.TypeOperationInLog;

public interface LogOcService {
    void log(TypeEntry typeEntry, TypeOperationInLog typeOperationInLog);
}
