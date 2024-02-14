package org.rostfactory.auth.dto;

import lombok.Data;

@Data
public class UserDtoResponse {
    private Long id;
    private String login;
    private String role;
}
