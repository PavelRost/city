package org.rostfactory.auth.controller;

import lombok.RequiredArgsConstructor;
import org.rostfactory.auth.dto.JwtDtoResponse;
import org.rostfactory.auth.dto.UserDtoRequest;
import org.rostfactory.auth.dto.UserDtoRequestCreate;
import org.rostfactory.auth.dto.UserDtoResponse;
import org.rostfactory.auth.entity.User;
import org.rostfactory.auth.mapper.UserMapper;
import org.rostfactory.auth.service.JwtService;
import org.rostfactory.auth.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtServiceImpl;
    private final UserMapper mapper;

    @GetMapping("/findByLogin/{login}")
    public UserDtoResponse findUserByLogin(@PathVariable String login) {
        return mapper.toDto(userService.findByLogin(login));
    }

    @GetMapping("/validateToken")
    public boolean validateToken(@RequestParam String token) {
        return jwtServiceImpl.validateAccessToken(token);
    }

    @PostMapping("/create")
    public UserDtoResponse createUser(@RequestBody UserDtoRequestCreate user) {
        User userEntity = mapper.toEntityForCreate(user);
        return mapper.toDto(userService.create(userEntity));
    }

    @PatchMapping("/updatePassword")
    public UserDtoResponse updatePassword(@RequestBody UserDtoRequest user) {
        User userEntity = mapper.toEntity(user);
        return mapper.toDto(userService.updatePassword(userEntity));
    }

    @DeleteMapping("/delete/{login}")
    public void deleteByLogin(@PathVariable String login) {
        userService.deleteByLogin(login);
    }

    @PostMapping("/authorization")
    public JwtDtoResponse authorization(@RequestBody UserDtoRequest user) {
        User userEntity = mapper.toEntity(user);
        String token = userService.authorization(userEntity);
        return JwtDtoResponse.builder().accessToken(token).build();
    }
}
