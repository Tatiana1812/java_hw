package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import ru.otus.repository.UsersRepository;

/**
 * Адаптер пользователей приложения к Spring Security.
 *
 * <p>Загрузка пользователя по логину из БД.</p>
 */
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    /**
     * Загружает пользователя по логину для Spring Security.
     *
     * @param username - логин
     * @return UserDetails для аутентификации
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = usersRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.withUsername(user.getLogin())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}