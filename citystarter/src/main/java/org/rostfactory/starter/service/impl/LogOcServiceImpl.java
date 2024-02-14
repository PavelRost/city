package org.rostfactory.starter.service.impl;

import org.rostfactory.sharemodule.dto.EntryChangeDtoRequest;
import org.rostfactory.sharemodule.enums.TypeEntry;
import org.rostfactory.sharemodule.enums.TypeOperationInLog;
import org.rostfactory.starter.service.LogOcService;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;

@Service
public class LogOcServiceImpl implements LogOcService {
    private final StompSession stompSession;

    public LogOcServiceImpl(StompSession stompSession) {
        this.stompSession = stompSession;
    }

    @Override
    public void log(TypeEntry typeEntry, TypeOperationInLog typeOperationInLog) {
        stompSession.send("/logger/entry", EntryChangeDtoRequest.builder()
                .typeEntry(typeEntry)
                .typeOperation(typeOperationInLog)
                .build());
    }
}
