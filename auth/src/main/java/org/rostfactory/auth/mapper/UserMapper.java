package org.rostfactory.auth.mapper;

import org.mapstruct.Mapper;
import org.rostfactory.auth.dto.UserDtoRequest;
import org.rostfactory.auth.dto.UserDtoRequestCreate;
import org.rostfactory.auth.dto.UserDtoResponse;
import org.rostfactory.auth.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDtoResponse toDto(User user);
    User toEntity(UserDtoRequest userDtoRequest);
    User toEntityForCreate(UserDtoRequestCreate userDtoRequestCreate);
}
