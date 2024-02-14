package org.rostfactory.auth.dto;

import lombok.Data;

@Data
public class UserDtoRequest {
    private String login;
    private String password;
}
