package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        if (ids != null && !ids.isEmpty()) {
            List<User> users = userRepository.findUsersByIdIn(ids);

            return users.stream()
                    .map(UserMapper::mapToUserDto)
                    .toList();
        } else {
            Pageable pageable = PageRequest.of(from / size, size);
            Page<User> users = userRepository.findUserBy(pageable);

            return users.stream()
                    .map(UserMapper::mapToUserDto)
                    .toList();
        }
    }

    @Override
    public UserDto addUser(NewUserRequest user) {
        log.info("Начало работы метода addUser");
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("Такая почта уже существует", "");
        }

        User newUser = userRepository.save(UserMapper.mapToUser(user));

        return UserMapper.mapToUserDto(newUser);
    }

    @Override
    public void deleteUser(int userId) {
        log.info("Начало работы метода deleteUser");
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователя нет", ""));

        userRepository.deleteById(userId);
    }
}
