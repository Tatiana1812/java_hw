package ru.otus.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import ru.otus.dto.request.AuthRequest;
import ru.otus.dto.response.AuthResponse;
import ru.otus.entity.Users;
import ru.otus.controller.exception.*;
import ru.otus.repository.UsersRepository;
import ru.otus.sessionmanager.TransactionManager;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TransactionManager transactionManager;

    public Users register(AuthRequest req) {
        Users user = new Users();
        user.setLogin(req.login());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setCreatedAt(LocalDateTime.now());
        transactionManager.doInTransaction(() -> usersRepository.save(user));
        return user;
    }

    public void login(AuthRequest req, HttpServletRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.login(), req.password())
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        request.getSession(true).setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
        );
    }

    public AuthResponse profile(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException();
        }

        Users u = usersRepository.findByLogin(auth.getName())
                .orElseThrow(UnauthorizedException::new);

        return new AuthResponse(u.getId(), u.getLogin());
    }
}