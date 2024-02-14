package org.rostfactory.auth.dto;

import lombok.Data;

@Data
public class UserDtoRequestCreate {
    private String login;
    private String password;
    private String role;
}
