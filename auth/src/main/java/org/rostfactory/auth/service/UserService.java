package org.rostfactory.auth.service;

import org.rostfactory.auth.entity.User;

public interface UserService {
    User create(User user);
    User updatePassword(User user);
    void deleteByLogin(String login);
    User findByLogin(String login);
    String authorization(User user);
}
