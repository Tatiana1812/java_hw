package ru.otus.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.config.PostgresContainerBase;
import ru.otus.dto.request.ReservationRequest;
import ru.otus.dto.response.ReservationResponse;
import ru.otus.entity.Users;
import ru.otus.repository.ReservationsRepository;
import ru.otus.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ReservationConcurrencyTest extends PostgresContainerBase {

    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationsRepository reservationsRepository;
    @Autowired
    UsersRepository usersRepository;

    @BeforeEach
    void setUp() {
        reservationsRepository.deleteAll();
    }

    @Test
    @Timeout(10)
    void twoUsersOneTable() throws Exception {
        List<Authentication> auths = ensureUsersAuth(2);

        LocalDateTime start = reservationDate();

        List<Object> results = runConcurrent(
                2,
                List.of(
                        () -> tryCreate(req("First", "+898090", 8, start), auths.get(0)),
                        () -> tryCreate(req("Second", "+555222", 8, start), auths.get(1))
                )
        );

        assertOutcome(results, 1, 1);
    }

    @Test
    @Timeout(10)
    void nUsersOneTable() throws Exception {
        int n = 10;
        List<Authentication> auths = ensureUsersAuth(n);

        LocalDateTime start = reservationDate();

        List<Callable<Object>> tasks = IntStream.range(0, n)
                .mapToObj(i -> (Callable<Object>) () ->
                        tryCreate(req("Guest" + i, "+7996239123" + i, 8, start), auths.get(i))
                )
                .toList();

        List<Object> results = runConcurrent(n, tasks);

        assertOutcome(results, 1, n - 1);
    }

    @Test
    @Timeout(10)
    void fourUsersThreeTables() throws Exception {
        List<Authentication> auths = ensureUsersAuth(4);

        LocalDateTime start = reservationDate();

        List<Object> results = runConcurrent(
                4,
                List.of(
                        () -> tryCreate(req("First", "+111222333", 6, start), auths.get(0)),
                        () -> tryCreate(req("Second", "+222333111", 6, start), auths.get(1)),
                        () -> tryCreate(req("Third", "+3331112222", 6, start), auths.get(2)),
                        () -> tryCreate(req("Fourth", "+333111444", 6, start), auths.get(3))
                )
        );

        assertOutcome(results, 3, 1);
    }

    @Test
    @Timeout(10)
    void nUsersThreeTables() throws Exception {
        int n = 8;
        List<Authentication> auths = ensureUsersAuth(n);

        LocalDateTime start = reservationDate();

        List<Callable<Object>> tasks = IntStream.range(0, n)
                .mapToObj(i -> (Callable<Object>) () ->
                        tryCreate(req("Guest" + i, "+7996239123" + i, 6, start), auths.get(i))
                )
                .toList();

        List<Object> results = runConcurrent(n, tasks);

        assertOutcome(results, 3, n - 3);
    }

    @Test
    @Timeout(10)
    void nUsersAllTables() throws Exception {
        int n = 10;
        List<Authentication> auths = ensureUsersAuth(n);

        LocalDateTime start = reservationDate();

        List<Callable<Object>> tasks = IntStream.range(0, n)
                .mapToObj(i -> (Callable<Object>) () ->
                        tryCreate(req("Guest" + i, "+7996239123" + i, 1, start), auths.get(i))
                )
                .toList();

        List<Object> results = runConcurrent(n, tasks);

        assertOutcome(results, 9, n - 9);
    }

    private ReservationRequest req(String guestName, String guestPhone, int persons, LocalDateTime startTime) {
        return new ReservationRequest(guestName, guestPhone, persons, startTime);
    }

    private LocalDateTime reservationDate() {
        return LocalDateTime.now()
                .plusDays(1)
                .withHour(12).withMinute(0).withSecond(0).withNano(0);
    }


    private List<Authentication> ensureUsersAuth(int n) {
        for (int i = 1; i <= n; i++) {
            String login = "user_it_" + i;
            usersRepository.findByLogin(login).orElseGet(() -> {
                Users u = new Users();
                u.setLogin(login);
                u.setPassword(login);
                return usersRepository.save(u);
            });
        }
        usersRepository.flush();

        return IntStream.rangeClosed(1, n)
                .mapToObj(i -> auth("user_it_" + i))
                .toList();
    }

    private Authentication auth(String login) {
        var token = new TestingAuthenticationToken(login, "N/A", "ROLE_USER");
        token.setAuthenticated(true);
        return token;
    }

    private Object tryCreate(ReservationRequest req, Authentication auth) {
        try {
            return reservationService.createReservation(req, auth);
        } catch (Throwable t) {
            return t;
        }
    }

    private List<Object> runConcurrent(int n, List<Callable<Object>> tasks) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(n);

        CountDownLatch ready = new CountDownLatch(n);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(n);

        ConcurrentLinkedQueue<Object> results = new ConcurrentLinkedQueue<>();

        for (Callable<Object> t : tasks) {
            pool.submit(() -> {
                ready.countDown();
                start.await();
                try {
                    results.add(t.call());
                } finally {
                    done.countDown();
                }
                return null;
            });
        }

        boolean allReady = ready.await(5, TimeUnit.SECONDS);
        assertThat(allReady).isTrue();

        start.countDown();

        boolean finished = done.await(10, TimeUnit.SECONDS);
        assertThat(finished).isTrue();

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);

        return List.copyOf(results);
    }

    private void assertOutcome(List<Object> results, int expectedSuccess, int expectedFail) {
        AtomicInteger success = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();

        for (Object r : results) {
            if (r instanceof ReservationResponse) success.incrementAndGet();
            else if (r instanceof Throwable) fail.incrementAndGet();
        }

        assertThat(success.get()).isEqualTo(expectedSuccess);
        assertThat(fail.get()).isEqualTo(expectedFail);

        assertThat(reservationsRepository.count()).isEqualTo(expectedSuccess);
    }
}