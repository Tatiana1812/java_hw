package ru.otus.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import ru.otus.entity.Users;
import ru.otus.repository.UsersRepository;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserDetailsServiceTest {

    @Mock
    private UsersRepository usersRepository;

    private AppUserDetailsService service;

    @BeforeEach
    void setUp() {
        service = new AppUserDetailsService(usersRepository);
    }

    @Test
    void loadUserByUsernameShouldReturnUserDetails() {
        Users user = new Users();
        user.setLogin("user1");
        user.setPassword("encoded");
        when(usersRepository.findByLogin("user1")).thenReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername("user1");

        assertThat(result.getUsername()).isEqualTo("user1");
        assertThat(result.getPassword()).isEqualTo("encoded");
        assertThat(result.getAuthorities()).extracting("authority").contains("ROLE_USER");
    }

    @Test
    void loadUserByUsernameShouldThrowWhenUserMissing() {
        when(usersRepository.findByLogin("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("missing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }
}
