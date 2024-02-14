package org.rostfactory.auth.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.rostfactory.auth.entity.User;
import org.rostfactory.auth.repository.UserRepository;
import org.rostfactory.auth.service.JwtService;
import org.rostfactory.auth.service.UserService;
import org.rostfactory.sharemodule.exception.PasswordIncorrectException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final JwtService jwtServiceImpl;

    @Override
    public User create(User user) {
        String passwordEncoded = encoder.encode(user.getPassword());
        user.setPassword(passwordEncoded);
        return repository.save(user);
    }

    @Override
    public User updatePassword(User user) {
        User userDB = findByLogin(user.getLogin());
        String passwordEncoded = encoder.encode(user.getPassword());
        userDB.setPassword(passwordEncoded);
        return repository.save(userDB);
    }

    @Override
    public void deleteByLogin(String login) {
        User user = findByLogin(login);
        repository.delete(user);
    }

    @Override
    public User findByLogin(String login) {
        return repository.findByLogin(login).orElseThrow(EntityNotFoundException::new);
    }


    @Override
    public String authorization(User user) {
        User userDB = findByLogin(user.getLogin());
        if (!encoder.matches(user.getPassword(), userDB.getPassword())) throw new PasswordIncorrectException();
        return jwtServiceImpl.generateAccessToken(user);
    }
}
