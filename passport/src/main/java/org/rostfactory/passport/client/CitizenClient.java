package org.rostfactory.passport.client;

import org.rostfactory.passport.dto.CitizenDtoResponse;

import java.util.List;

public interface CitizenClient {
    List<CitizenDtoResponse> findCitizensByLastNameStartLetter(String letter);
}
