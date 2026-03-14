package ru.otus.service;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.config.PostgresContainerBase;
import ru.otus.controller.exception.UnauthorizedException;
import ru.otus.dto.request.AuthRequest;
import ru.otus.dto.response.AuthResponse;
import ru.otus.entity.Users;
import ru.otus.repository.UsersRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceImplTest extends PostgresContainerBase {

     @Autowired
     UsersRepository usersRepository;
     @Autowired
     AuthService authService;

    @Test
    @Order(1)
    void registerShouldEncodePasswordSaveUserAndReturnUser() {
        AuthRequest request = new AuthRequest("user", "password");

        Users response = authService.register(request);

        assertThat(response.getLogin()).isEqualTo("user");
        assertThat(response.getPassword()).isNotBlank();
        assertThat(response.getCreatedAt()).isNotNull();

        Users user = usersRepository.findByLogin("user").orElseThrow();

        assertThat(usersRepository.count()).isEqualTo(3);
    }

    @Test
    void loginShouldAuthenticateAndStoreContextInSession() {
        AuthRequest request = new AuthRequest("user", "password");
        var httpServletRequest = new MockHttpServletRequest();

        assertDoesNotThrow(() -> authService.login(request, httpServletRequest));

        HttpSession session = httpServletRequest.getSession(false);
        SecurityContext attributes = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        assertThat(session).isNotNull();
        assertThat(attributes.getAuthentication()).isNotNull();
        assertThat(attributes.getAuthentication().getName()).isEqualTo("user");
        assertThat(attributes.getAuthentication().isAuthenticated()).isTrue();
    }

    @Test
    void loginShouldNotAuthenticateAndStoreContextInSession() {
        AuthRequest request = new AuthRequest("useruser", "password");
        var httpServletRequest = new MockHttpServletRequest();

        assertThrows(AuthenticationException.class, () -> authService.login(request, httpServletRequest));
    }

    @Test
    void profileShouldThrowUnauthorizedWhenAuthenticationIsNull() {
        assertThrows(UnauthorizedException.class, () -> authService.profile(null));
    }

    @Test
    void profileShouldThrowUnauthorizedWhenUserNotFound() {
        Authentication authentication = new TestingAuthenticationToken("useruser", "x", "ROLE_USER");

        assertThrows(UnauthorizedException.class, () -> authService.profile(authentication));
    }

    @Test
    void profileShouldReturnAuthResponseWhenUserExists() {
        Authentication authentication = new TestingAuthenticationToken("user", "x", "ROLE_USER");

        AuthResponse response = authService.profile(authentication);

        assertThat(response.id()).isEqualTo(usersRepository.findByLogin("user").orElseThrow().getId());
        assertThat(response.login()).isEqualTo("user");
    }
}
