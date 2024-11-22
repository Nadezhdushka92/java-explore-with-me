package ru.practicum.user.mapper;

import ru.practicum.user.dto.CreateUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

public class UserMapper {

    public static User mapToUser(UserDto user) {

        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserDto mapToUserDto(User user) {

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserShortDto mapToUserShortDto(User user) {

        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User mapToUser(CreateUserRequest createUserRequest) {

        return User.builder()
                .email(createUserRequest.getEmail())
                .name(createUserRequest.getName())
                .build();
    }
}
