package org.rostfactory.citizen.client;

import org.rostfactory.sharemodule.dto.EntryDtoResponse;

import java.util.List;

public interface LogClient {
    List<EntryDtoResponse> getEntryEachTypeInLog();
}
