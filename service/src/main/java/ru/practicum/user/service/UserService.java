package ru.practicum.user.service;

import ru.practicum.user.dto.CreateUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Integer> ids, int from, int size);

    UserDto addUser(CreateUserRequest user);

    void deleteUser(int userId);

}
