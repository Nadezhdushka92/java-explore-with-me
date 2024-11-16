package ru.practicum.user.service;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Integer> ids, int from, int size);

    UserDto addUser(NewUserRequest user);

    void deleteUser(int userId);

}
