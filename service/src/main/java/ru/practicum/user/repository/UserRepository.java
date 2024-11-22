package ru.practicum.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.user.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Page<User> findUserBy(Pageable pageable);

    List<User> findUsersByIdIn(List<Integer> ids);

    boolean existsByEmail(String email);
}
