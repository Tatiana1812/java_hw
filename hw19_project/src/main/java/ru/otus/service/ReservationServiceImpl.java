package ru.otus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.otus.controller.exception.NoAvailableTableException;
import ru.otus.controller.exception.ReservationNotFoundException;
import ru.otus.controller.exception.UnauthorizedException;
import ru.otus.dto.request.ReservationRequest;
import ru.otus.dto.response.ReservationResponse;
import ru.otus.entity.Reservations;
import ru.otus.entity.Tables;
import ru.otus.entity.Users;
import ru.otus.enums.ReservationStatus;
import ru.otus.repository.ReservationsRepository;
import ru.otus.repository.TablesRepository;
import ru.otus.repository.UsersRepository;
import ru.otus.sessionmanager.TransactionManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис бронирования столиков.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private static final Duration DEFAULT_DURATION = Duration.ofHours(3);

    private final UsersRepository usersRepository;
    private final TablesRepository tablesRepository;
    private final ReservationsRepository reservationsRepository;
    private final TransactionManager transactionManager;

    /**
     * Создаёт бронирование для текущего пользователя.
     *
     * <p>Длительность брони по умолчанию: 3 часа ({@link #DEFAULT_DURATION}).</p>
     *
     * @param req - параметры брони
     * @param auth - аутентификация пользователя
     * @return созданная бронь
     */
    public ReservationResponse createReservation(ReservationRequest req, Authentication auth) {
        Users user = usersRepository.findByLogin(auth.getName())
                .orElseThrow(UnauthorizedException::new);

        LocalDateTime start = req.startTime();
        LocalDateTime end = start.plus(DEFAULT_DURATION);

        return reservationProcess(req, start, end, user);
    }

    /**
     * Возвращает список броней текущего пользователя.
     *
     * @param auth - аутентификация пользователя
     * @return список всех броней
     */
    public List<ReservationResponse> getAllReservations(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException();
        }

        String login = auth.getName();

        return reservationsRepository.findAllReservations(login).stream()
                .map(r -> new ReservationResponse(
                        r.getGuestName(),
                        r.getGuestPhone(),
                        r.getPersons(),
                        r.getId(),
                        r.getTable().getId(),
                        r.getStatus(),
                        r.getStartTime(),
                        r.getEndTime()
                )).toList();
    }

    /**
     * Отменяет бронь пользователя.
     *
     * @param reservationId - id брони
     * @param auth - аутентификация пользователя
     */
    public void cancelReservation(Long reservationId, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException();
        }
        String login = auth.getName();

        transactionManager.doInTransaction(() -> {
            Reservations r = reservationsRepository.findReservation(reservationId, login)
                    .orElseThrow(ReservationNotFoundException::new);

            if (r.getStatus() == ReservationStatus.CANCELLED) {
                log.info("Reservation already cancelled. id={}, user={}", reservationId, login);
            }

            r.setStatus(ReservationStatus.CANCELLED);
            return reservationsRepository.save(r);
        });
    }

    private ReservationResponse reservationProcess(ReservationRequest req, LocalDateTime start, LocalDateTime end, Users user) {
            List<Tables> candidates = tablesRepository.findAvailableTables(req.persons(), req.startTime(), end);
            if (candidates.isEmpty()) {
                log.info("No table candidates by capacity or time. persons={}, start={}, end={}",
                        req.persons(), start, end);
                throw new NoAvailableTableException();
            }

            for (Tables table : candidates) {
                try {
                    Reservations saved = createReservationAttempt(user, table, req, start, end);

                    log.info("Reservation table={}. persons={}, start={}, end={}",
                            table.getId(), req.persons(), start, end);

                    return new ReservationResponse(
                            saved.getGuestName(),
                            saved.getGuestPhone(),
                            saved.getPersons(),
                            saved.getId(),
                            saved.getTable().getId(),
                            saved.getStatus(),
                            saved.getStartTime(),
                            saved.getEndTime()
                    );

                } catch (DataAccessException ex) {
                    log.debug("Table is busy, trying next. tableId={}, persons={}, start={}, end={}",
                            table.getId(), req.persons(), start, end);
                }
            }
            log.info("No available tables for request. persons={}, start={}, end={}, candidates={}",
                    req.persons(), start, end, candidates.size());
            throw new NoAvailableTableException();
    }
    private Reservations createReservationAttempt(Users user, Tables table, ReservationRequest req,
                                   LocalDateTime start, LocalDateTime end) {
        return transactionManager.doInTransaction(() -> {
            Reservations r = new Reservations();
            r.setUser(user);
            r.setTable(table);
            r.setGuestName(req.guestName());
            r.setGuestPhone(req.guestPhone());
            r.setPersons(req.persons());
            r.setStatus(ReservationStatus.CONFIRMED);
            r.setStartTime(start);
            r.setEndTime(end);
            return reservationsRepository.saveAndFlush(r);
        });
    }

}