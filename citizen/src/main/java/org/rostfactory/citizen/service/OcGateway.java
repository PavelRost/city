package org.rostfactory.citizen.service;

import org.rostfactory.sharemodule.enums.TypeEntry;
import org.rostfactory.sharemodule.enums.TypeOperationInLog;

public interface OcGateway {
    void sendEntryInLog(TypeEntry typeEntry, TypeOperationInLog typeOperationInLog);
}
