package org.rostfactory.auth.service;

import org.rostfactory.auth.entity.User;

public interface JwtService {
    String generateAccessToken(User user);
    boolean validateAccessToken(String accessToken);

}
