package org.rostfactory.citizen.client.impl;

import lombok.RequiredArgsConstructor;
import org.rostfactory.citizen.client.LogClient;
import org.rostfactory.sharemodule.config.UrlConfigProperties;
import org.rostfactory.sharemodule.dto.EntryDtoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LogClientImpl implements LogClient {
    private final UrlConfigProperties urlConfigProperties;
    private final RestTemplate restTemplate;

    @Override
    public List<EntryDtoResponse> getEntryEachTypeInLog() {
        ResponseEntity<EntryDtoResponse[]> entryEachType = restTemplate.getForEntity("%s/getEntryEachType".formatted(urlConfigProperties.getLogServiceUrl()), EntryDtoResponse[].class);
        return Arrays.stream(entryEachType.getBody()).toList();
    }
}
