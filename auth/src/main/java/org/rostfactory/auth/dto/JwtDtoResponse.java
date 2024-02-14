package org.rostfactory.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtDtoResponse {
    private final String type = "Bearer";
    private String accessToken;
}
