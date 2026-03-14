package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.entity.Users;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByLogin(String login);
    boolean existsByLogin(String login);
}
