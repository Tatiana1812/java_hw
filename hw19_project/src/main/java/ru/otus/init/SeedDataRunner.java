package ru.otus.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.otus.entity.Users;
import ru.otus.repository.UsersRepository;

import java.time.LocalDateTime;

@Component
@Profile({"dev", "test"})
@RequiredArgsConstructor
public class SeedDataRunner implements ApplicationRunner {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        createUserIfAbsent("user1", "pwd123");
        createUserIfAbsent("user2", "pwd321");
    }

    private void createUserIfAbsent(String login, String rawPassword) {
        if (!usersRepository.existsByLogin(login)) {
            Users u = new Users();
            u.setLogin(login);
            u.setPassword(passwordEncoder.encode(rawPassword));
            usersRepository.save(u);
        }
    }
}